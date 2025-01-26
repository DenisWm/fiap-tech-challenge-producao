package com.fiap.tech.challenge.application.production.retrieve.get;

import com.fiap.tech.challenge.domain.production.Item;
import com.fiap.tech.challenge.domain.production.Production;

public record ItemOutput(
        String productId,
        String productName,
        int quantity
) {

    public static ItemOutput from(final Item aItem) {
        return new ItemOutput(
                aItem.productId(),
                aItem.productName(),
                aItem.quantity()
        );
    }
}
