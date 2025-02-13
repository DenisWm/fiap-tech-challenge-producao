package com.fiap.tech.challenge.application.production.retrieve.list;

import com.fiap.tech.challenge.domain.production.Item;
import com.fiap.tech.challenge.domain.production.Production;
import com.fiap.tech.challenge.domain.production.ProductionStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductionListOutputTest {

    @Test
    void givenProduction_whenCallsFrom_shouldReturnOutput() {
        final var expectedOrderId = UUID.randomUUID().toString();
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);
        final var expectedItems = List.of(expectedItem);

        final var aProduction = Production.createProduction(expectedOrderId, expectedItems);

        final var output = ProductionListOutput.from(aProduction);

        assertEquals(aProduction.getId().getValue(), output.id());
        assertEquals(expectedOrderId, output.orderId());
        assertEquals(aProduction.getStatus(), output.status());
        assertEquals(aProduction.getReceivedAt(), output.receivedAt());
        assertEquals(aProduction.getStartedAt(), output.startedAt());
    }

}