package com.fiap.tech.challenge.domain.production;

import com.fiap.tech.challenge.domain.exceptions.DomainException;
import com.fiap.tech.challenge.domain.exceptions.NotificationException;
import com.fiap.tech.challenge.domain.validation.handler.Notification;
import com.fiap.tech.challenge.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductionTest {


    @Test
    void givenValidParam_whenCallsCreateProduction_shouldReturnIt() {
        final var expectedOrderId = UUID.randomUUID().toString();
        final var expectedStatus = ProductionStatus.RECEIVED;
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);

        final var expectedItems = List.of(expectedItem);

        final var actualProduction = Production.createProduction(expectedOrderId, expectedItems);

        assertDoesNotThrow(() -> actualProduction.validate(new ThrowsValidationHandler()));

        assertNotNull(actualProduction);
        assertNotNull(actualProduction.getId());
        assertEquals(expectedOrderId, actualProduction.getOrderId());
        assertEquals(expectedStatus, actualProduction.getStatus());
        assertTrue(expectedItems.containsAll(actualProduction.getItems())
                && actualProduction.getItems().size() == expectedItems.size());
        assertNotNull(actualProduction.getReceivedAt());
        assertNull(actualProduction.getStartedAt());
        assertNull(actualProduction.getFinishedAt());
    }

    @Test
    void givenNullOrderId_whenCallsCreateProduction_shouldThrowException() {
        final String expectedOrderId = null;
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);
        final var expectedErrorMessage = "'orderId' should not be null";
        final var expectedErrorCount = 1;

        final var expectedItems = List.of(expectedItem);

        final var actualProduction = Production.createProduction(expectedOrderId, expectedItems);

        final var actualException = assertThrows(DomainException.class,
                () -> actualProduction.validate(new ThrowsValidationHandler()));

        assertNotNull(actualException);

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenEmptyOrderId_whenCallsCreateProduction_shouldThrowException() {
        final var expectedOrderId = "";
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);
        final var expectedErrorMessage = "'orderId' should not be empty";
        final var expectedErrorCount = 1;

        final var expectedItems = List.of(expectedItem);

        final var actualProduction = Production.createProduction(expectedOrderId, expectedItems);

        final var actualException = assertThrows(DomainException.class,
                () -> actualProduction.validate(new ThrowsValidationHandler()));

        assertNotNull(actualException);

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenEmptyItem_whenCallsCreateProduction_shouldThrowException() {
        final var expectedOrderId = UUID.randomUUID().toString();
        final var expectedErrorMessage = "'items' should not be empty";
        final var expectedErrorCount = 1;

        final var expectedItems = List.<Item>of();

        final var actualProduction = Production.createProduction(expectedOrderId, expectedItems);

        final var actualException = assertThrows(DomainException.class,
                () -> actualProduction.validate(new ThrowsValidationHandler()));

        assertNotNull(actualException);

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenValidReadyStatus_whenCallsUpdateStatus_shouldReturnIt() {
        final var expectedOrderId = UUID.randomUUID().toString();
        final var expectedStatus = ProductionStatus.READY;
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);

        final var expectedItems = List.of(expectedItem);
        final var expectedDomainEventsSize = 1;

        final var aProduction = Production.createProduction(expectedOrderId, expectedItems);

        assertDoesNotThrow(() -> aProduction.validate(new ThrowsValidationHandler()));

        final var actualProduction = Production.with(aProduction).updateStatus(expectedStatus);

        assertDoesNotThrow(() -> aProduction.validate(new ThrowsValidationHandler()));

        assertNotNull(actualProduction);
        assertNotNull(actualProduction.getId());

        assertEquals(expectedOrderId, actualProduction.getOrderId());

        assertEquals(aProduction.getOrderId(), actualProduction.getOrderId());

        assertEquals(expectedStatus, actualProduction.getStatus());

        assertNotEquals(aProduction.getStatus(), actualProduction.getStatus());

        assertTrue(expectedItems.containsAll(actualProduction.getItems())
                && actualProduction.getItems().size() == expectedItems.size());

        assertEquals(actualProduction.getReceivedAt(), aProduction.getReceivedAt());

        assertEquals(actualProduction.getStartedAt(), aProduction.getStartedAt());

        assertNotNull(actualProduction.getFinishedAt());

        assertEquals(expectedDomainEventsSize, actualProduction.getDomainEvents().size());
        final var actualEvent = (ProductionStatusChanged) actualProduction.getDomainEvents().get(0);
        assertEquals(actualProduction.getOrderId(), actualEvent.getOrderId());
        assertEquals(actualProduction.getStatus().name().toLowerCase(), actualEvent.getStatus());
        assertEquals(actualProduction.getStartedAt(), actualEvent.getStartedAt());
        assertEquals(actualProduction.getFinishedAt(), actualEvent.getFinishedAt());
    }

    @Test
    void givenValidInPreparationStatus_whenCallsUpdateStatus_shouldReturnIt() {
        final var expectedOrderId = UUID.randomUUID().toString();
        final var expectedStatus = ProductionStatus.IN_PREPARATION;
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);

        final var expectedItems = List.of(expectedItem);
        final var expectedDomainEventsSize = 1;

        final var aProduction = Production.createProduction(expectedOrderId, expectedItems);

        assertDoesNotThrow(() -> aProduction.validate(new ThrowsValidationHandler()));

        final var actualProduction = Production.with(aProduction).updateStatus(expectedStatus);

        assertDoesNotThrow(() -> aProduction.validate(new ThrowsValidationHandler()));

        assertNotNull(actualProduction);
        assertNotNull(actualProduction.getId());

        assertEquals(expectedOrderId, actualProduction.getOrderId());

        assertEquals(aProduction.getOrderId(), actualProduction.getOrderId());

        assertEquals(expectedStatus, actualProduction.getStatus());

        assertNotEquals(aProduction.getStatus(), actualProduction.getStatus());

        assertTrue(expectedItems.containsAll(actualProduction.getItems())
                && actualProduction.getItems().size() == expectedItems.size());

        assertEquals(actualProduction.getReceivedAt(), aProduction.getReceivedAt());

        assertNotNull(actualProduction.getStartedAt());

        assertEquals(expectedDomainEventsSize, actualProduction.getDomainEvents().size());
        final var actualEvent = (ProductionStatusChanged) actualProduction.getDomainEvents().get(0);
        assertEquals(actualProduction.getOrderId(), actualEvent.getOrderId());
        assertEquals(actualProduction.getStatus().name().toLowerCase(), actualEvent.getStatus());
        assertEquals(actualProduction.getStartedAt(), actualEvent.getStartedAt());
        assertEquals(actualProduction.getFinishedAt(), actualEvent.getFinishedAt());
    }
}