package com.fiap.tech.challenge.application.production.create;

import com.fiap.tech.challenge.domain.production.Item;

import java.time.Instant;
import java.util.List;

public record CreateProductionCommand(
        String orderId,
        List<Item> items
) {

    public static CreateProductionCommand from(
            final String orderId,
            final List<Item> items
    ) {
        return new CreateProductionCommand(orderId, items);
    }
}
