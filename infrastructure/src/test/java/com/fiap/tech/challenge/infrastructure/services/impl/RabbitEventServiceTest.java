package com.fiap.tech.challenge.infrastructure.services.impl;

import com.fiap.tech.challenge.domain.production.ProductionStatusChanged;
import com.fiap.tech.challenge.infrastructure.AmqpTest;
import com.fiap.tech.challenge.infrastructure.configuration.Json;
import com.fiap.tech.challenge.infrastructure.configuration.annotations.ProductionStatusChangedQueue;
import com.fiap.tech.challenge.infrastructure.services.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.TimeUnit;



@AmqpTest
public class RabbitEventServiceTest {

    private static final String LISTENER = "production.status-changed";
    @Autowired
    @ProductionStatusChangedQueue
    private EventService publisher;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    public void shouldSendMessage() throws InterruptedException {

//        Instant now = Instant.now();
//        final var notification = new ProductionStatusChanged("123", "ready", now, now);
//
//        final var expectedMessage = Json.writeValueAsString(notification);
//
//        this.publisher.send(notification);
//
//        final var invocationData = harness.getNextInvocationDataFor(LISTENER, 1, TimeUnit.SECONDS);
//
//        assertNotNull(invocationData);
//        assertNotNull(invocationData.getArguments());
//
//        final var actualMessage = (String) invocationData.getArguments()[0];
//
//        assertEquals(expectedMessage, actualMessage);
    }

    @Component
    static class VideoCreatedNewsListener {

        @RabbitListener(id = LISTENER, queues = "${amqp.queues.production-status-changed.routing-key}")
        void onProductionStatusChanged(@Payload String message) {

        }
    }
}
