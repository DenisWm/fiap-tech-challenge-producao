package com.fiap.tech.challenge.application.production.update;

import com.fiap.tech.challenge.application.production.UseCaseTest;
import com.fiap.tech.challenge.domain.production.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateProductionStatusUseCaseTest extends UseCaseTest {

    @Mock
    private ProductionGateway productionGateway;

    @InjectMocks
    private DefaultUpdateProductionStatusUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(productionGateway);
    }

    @Test
    void givenAValidInPreparationStatus_whenCallsUpdate_shouldReturnId() {
        final var expectedOrderId = UUID.randomUUID().toString();
        final var expectedStatus = ProductionStatus.IN_PREPARATION;
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);
        final var expectedItems = List.of(expectedItem);
        final var expectedDomainEventCount = 1;

        final var aProduction = Production.createProduction(expectedOrderId, expectedItems);

        final var expectedId = aProduction.getId();
        final var aCmd = UpdateProductionStatusCommand.with(expectedId.getValue(), expectedStatus.name());

        when(productionGateway.findById(eq(expectedId))).thenReturn(Optional.of(Production.with(aProduction)));

        when(productionGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aResult = useCase.execute(aCmd);

        assertNotNull(aResult);
        assertEquals(expectedId.getValue(), aResult.id());

        final var captor = ArgumentCaptor.forClass(Production.class);

        verify(productionGateway).update(captor.capture());

        final var actualProduction = captor.getValue();

        assertEquals(expectedOrderId, actualProduction.getOrderId());
        assertTrue(expectedItems.containsAll(actualProduction.getItems()) &&
                expectedItems.size() == actualProduction.getItems().size());
        assertEquals(expectedStatus, actualProduction.getStatus());
        assertEquals(aProduction.getReceivedAt(), actualProduction.getReceivedAt());
        assertNotNull(actualProduction.getStartedAt());
        assertNull(actualProduction.getFinishedAt());

        assertEquals(expectedDomainEventCount, actualProduction.getDomainEvents().size());
        final var actualEvent = (ProductionStatusChanged) actualProduction.getDomainEvents().get(0);
        assertEquals(actualProduction.getOrderId(), actualEvent.getOrderId());
        assertEquals(actualProduction.getStatus().name().toLowerCase(), actualEvent.getStatus());
        assertEquals(actualProduction.getStartedAt(), actualEvent.getStartedAt());
        assertEquals(actualProduction.getFinishedAt(), actualEvent.getFinishedAt());
    }

    @Test
    void givenAValidReadyStatus_whenCallsUpdate_shouldReturnId() {
        final var expectedOrderId = UUID.randomUUID().toString();
        final var expectedStatus = ProductionStatus.READY;
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);
        final var expectedItems = List.of(expectedItem);
        final var expectedDomainEventCount = 1;

        final var aProduction = Production.createProduction(expectedOrderId, expectedItems);

        aProduction.updateStatus(ProductionStatus.IN_PREPARATION);

        final var expectedId = aProduction.getId();
        final var aCmd = UpdateProductionStatusCommand.with(expectedId.getValue(), expectedStatus.name());

        when(productionGateway.findById(eq(expectedId))).thenReturn(Optional.of(Production.with(aProduction)));

        when(productionGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aResult = useCase.execute(aCmd);

        assertNotNull(aResult);
        assertEquals(expectedId.getValue(), aResult.id());

        final var captor = ArgumentCaptor.forClass(Production.class);

        verify(productionGateway).update(captor.capture());

        final var actualProduction = captor.getValue();

        assertEquals(expectedOrderId, actualProduction.getOrderId());
        assertTrue(expectedItems.containsAll(actualProduction.getItems()) &&
                expectedItems.size() == actualProduction.getItems().size());
        assertEquals(expectedStatus, actualProduction.getStatus());
        assertEquals(aProduction.getReceivedAt(), actualProduction.getReceivedAt());
        assertEquals(aProduction.getStartedAt(), actualProduction.getStartedAt());
        assertNotNull(actualProduction.getFinishedAt());

        assertEquals(expectedDomainEventCount, actualProduction.getDomainEvents().size());
        final var actualEvent = (ProductionStatusChanged) actualProduction.getDomainEvents().get(0);
        assertEquals(actualProduction.getOrderId(), actualEvent.getOrderId());
        assertEquals(actualProduction.getStatus().name().toLowerCase(), actualEvent.getStatus());
        assertEquals(actualProduction.getStartedAt(), actualEvent.getStartedAt());
        assertEquals(actualProduction.getFinishedAt(), actualEvent.getFinishedAt());
    }
}