package com.fiap.tech.challenge.infrastructure.amqp;

import com.fiap.tech.challenge.application.production.create.CreateProductionCommand;
import com.fiap.tech.challenge.application.production.create.CreateProductionUseCase;
import com.fiap.tech.challenge.domain.production.Item;
import com.fiap.tech.challenge.infrastructure.AmqpTest;
import com.fiap.tech.challenge.infrastructure.configuration.Json;
import com.fiap.tech.challenge.infrastructure.configuration.annotations.OrderPayedQueue;
import com.fiap.tech.challenge.infrastructure.configuration.properties.amqp.QueueProperties;
import com.fiap.tech.challenge.infrastructure.production.models.ItemResult;
import com.fiap.tech.challenge.infrastructure.production.models.OrderPayedResult;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AmqpTest
class OrderPayedListenerTest {


    @Autowired
    private TestRabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @MockBean
    private CreateProductionUseCase createProductionUseCase;

    @Autowired
    @OrderPayedQueue
    private QueueProperties queueProperties;

    @Test
    public void givenOrderPayedResult_whenCallsListener_shouldProcess() throws InterruptedException {
        final var expectedOrderId = UUID.randomUUID().toString();
        final var expectedItems = List.of(new ItemResult("productId", "productName", 3));
        final var expectedOccurredOn = Instant.now();
        final var expectedResult = new OrderPayedResult(
                expectedOrderId,
                expectedItems,
                expectedOccurredOn
        );

        final var expectedMessage = Json.writeValueAsString(expectedResult);

        when(createProductionUseCase.execute(any())).thenAnswer(returnsFirstArg());

        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        final var invocationData = harness.getNextInvocationDataFor(OrderPayedListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        assertNotNull(invocationData);
        assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];

        assertEquals(expectedMessage, actualMessage);

        final var cmdCaptor = ArgumentCaptor.forClass(CreateProductionCommand.class);
        verify(createProductionUseCase).execute(cmdCaptor.capture());

        final var actualCommand = cmdCaptor.getValue();

        assertEquals(expectedOrderId, actualCommand.orderId());
        assertEquals(expectedItems.stream()
                .map(item -> Item.of(item.productId(), item.productName(), item.quantity())).toList(), actualCommand.items());
    }
}