package com.fiap.tech.challenge.application.production.create;

import com.fiap.tech.challenge.application.production.UseCaseTest;
import com.fiap.tech.challenge.domain.exceptions.NotificationException;
import com.fiap.tech.challenge.domain.production.Item;
import com.fiap.tech.challenge.domain.production.Production;
import com.fiap.tech.challenge.domain.production.ProductionGateway;
import com.fiap.tech.challenge.domain.production.ProductionStatus;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateProductionUseCaseTest extends UseCaseTest {

    @Mock
    private ProductionGateway productionGateway;

    @InjectMocks
    private DefaultCreateProductionUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(productionGateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreate_shouldReturnId() {
        final var expectedOrderId = UUID.randomUUID().toString();
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);
        final var expectedItems = List.of(expectedItem);

        final var aCmd = CreateProductionCommand.from(expectedOrderId, expectedItems);

        when(productionGateway.create(any())).thenAnswer(returnsFirstArg());

        final var aResult = useCase.execute(aCmd);

        assertNotNull(aResult);

        final var captor = ArgumentCaptor.forClass(Production.class);

        verify(productionGateway).create(captor.capture());

        final var aProduction = captor.getValue();

        assertEquals(expectedOrderId, aProduction.getOrderId());
        assertTrue(expectedItems.containsAll(aProduction.getItems()) &&
                expectedItems.size() == aProduction.getItems().size());
        assertEquals(ProductionStatus.RECEIVED, aProduction.getStatus());
        assertNotNull(aProduction.getReceivedAt());
        assertNull(aProduction.getStartedAt());
        assertNull(aProduction.getFinishedAt());
    }

    @Test
    void givenANullOrderId_whenCallsCreate_shouldReturnError() {
        final String expectedOrderId = null;
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);
        final var expectedItems = List.of(expectedItem);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'orderId' should not be null";
        final var expectedExceptionMessage = "Could not create Aggregate Production";

        final var aCmd = CreateProductionCommand.from(expectedOrderId, expectedItems);

        final var aResult = assertThrows(NotificationException.class, () -> useCase.execute(aCmd));

        assertNotNull(aResult);
        assertEquals(expectedExceptionMessage, aResult.getMessage());
        assertEquals(expectedErrorCount, aResult.getErrors().size());
        assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
    }

    @Test
    void givenAnEmptyOrderId_whenCallsCreate_shouldReturnError() {
        final var expectedOrderId = "";
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);
        final var expectedItems = List.of(expectedItem);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'orderId' should not be empty";
        final var expectedExceptionMessage = "Could not create Aggregate Production";

        final var aCmd = CreateProductionCommand.from(expectedOrderId, expectedItems);

        final var aResult = assertThrows(NotificationException.class, () -> useCase.execute(aCmd));

        assertNotNull(aResult);
        assertEquals(expectedExceptionMessage, aResult.getMessage());
        assertEquals(expectedErrorCount, aResult.getErrors().size());
        assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
    }

    @Test
    void givenEmptyItems_whenCallsCreate_shouldReturnError() {
        final var expectedOrderId = UUID.randomUUID().toString();
        final var expectedItems = List.<Item>of();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'items' should not be empty";
        final var expectedExceptionMessage = "Could not create Aggregate Production";

        final var aCmd = CreateProductionCommand.from(expectedOrderId, expectedItems);

        final var aResult = assertThrows(NotificationException.class, () -> useCase.execute(aCmd));

        assertNotNull(aResult);
        assertEquals(expectedExceptionMessage, aResult.getMessage());
        assertEquals(expectedErrorCount, aResult.getErrors().size());
        assertEquals(expectedErrorMessage, aResult.getErrors().get(0).message());
    }
}