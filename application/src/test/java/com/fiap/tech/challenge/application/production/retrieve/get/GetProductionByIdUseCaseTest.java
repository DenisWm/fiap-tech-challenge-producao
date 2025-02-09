package com.fiap.tech.challenge.application.production.retrieve.get;

import com.fiap.tech.challenge.application.production.UseCaseTest;
import com.fiap.tech.challenge.domain.exceptions.NotFoundException;
import com.fiap.tech.challenge.domain.production.Item;
import com.fiap.tech.challenge.domain.production.Production;
import com.fiap.tech.challenge.domain.production.ProductionGateway;
import com.fiap.tech.challenge.domain.production.ProductionStatus;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GetProductionByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetProductionByIdUseCase useCase;

    @Mock
    private ProductionGateway productionGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(productionGateway);
    }

    @Test
    void givenAnExistentId_whenCallsGet_shouldReturnIt() {
        final var expectedOrderId = UUID.randomUUID().toString();
        final var expectedStatus = ProductionStatus.RECEIVED;
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);
        final var expectedItems = List.of(expectedItem);

        final var aProduction = Production.createProduction(expectedOrderId, expectedItems);

        final var expectedId = aProduction.getId().getValue();

        when(productionGateway.findById(any())).thenReturn(Optional.of(Production.with(aProduction)));

        final var actualProduction = useCase.execute(expectedId);

        assertEquals(expectedId, actualProduction.id());
        assertEquals(expectedOrderId, actualProduction.orderId());
        assertEquals(expectedItems.size(), actualProduction.items().size());
        assertEquals(expectedItem.productId(), actualProduction.items().get(0).productId());
        assertEquals(expectedItem.productName(), actualProduction.items().get(0).productName());
        assertEquals(expectedItem.quantity(), actualProduction.items().get(0).quantity());
        assertEquals(expectedStatus, actualProduction.status());
        assertNotNull(actualProduction.receivedAt());
        assertNull(actualProduction.startedAt());
        assertNull(actualProduction.finishedAt());

    }

    @Test
    void givenAnInvalidId_whenCallsGet_shouldReturnNotFound() {
        final var expectedId = "123";
        final var expectedErrorMessage = "Production with ID 123 was not found";

        when(productionGateway.findById(any())).thenReturn(Optional.empty());

        final var actualException = assertThrows(NotFoundException.class,() -> useCase.execute(expectedId));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

    }
}