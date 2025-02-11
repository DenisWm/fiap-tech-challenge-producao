package com.fiap.tech.challenge.infrastructure.amqp;

import com.fiap.tech.challenge.application.production.create.CreateProductionCommand;
import com.fiap.tech.challenge.application.production.create.CreateProductionUseCase;
import com.fiap.tech.challenge.domain.production.Item;
import com.fiap.tech.challenge.infrastructure.configuration.Json;
import com.fiap.tech.challenge.infrastructure.production.models.OrderPayedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class OrderPayedListener {

    private final static Logger log = LoggerFactory.getLogger(OrderPayedListener.class);

    public static final String LISTENER_ID = "orderPayedListener";

    private final CreateProductionUseCase createProductionUseCase;

    public OrderPayedListener(final CreateProductionUseCase createProductionUseCase) {
        this.createProductionUseCase = Objects.requireNonNull(createProductionUseCase);
    }

    @RabbitListener(id = LISTENER_ID, queues = "order.payed.queue")
    public void onProductionStatusChangedMessage(@Payload final String message) {
        try {
            final var aResult = Json.readValue(message, OrderPayedResult.class);

            log.info("[message:order.payed.income][payload:{}]", message);

            List<Item> items = aResult.items().stream()
                    .map(item -> Item.of(item.productId(), item.productName(), item.quantity()))
                    .toList();

            final var aCmd = CreateProductionCommand.with(
                    aResult.orderID(),
                    items
            );

            this.createProductionUseCase.execute(aCmd);

        } catch(Exception e) {
            log.error("[message:production.status.changed.income] [status:unknown] [payload:{}] [message:{}]", message, e.getMessage());
        }
    }
}
