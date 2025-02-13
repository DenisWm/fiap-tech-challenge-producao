package com.fiap.tech.challenge.infrastructure.production;

import com.fiap.tech.challenge.domain.pagination.SearchQuery;
import com.fiap.tech.challenge.domain.production.Item;
import com.fiap.tech.challenge.domain.production.Production;
import com.fiap.tech.challenge.domain.production.ProductionStatus;
import com.fiap.tech.challenge.infrastructure.IntegrationTest;
import com.fiap.tech.challenge.infrastructure.PostgresGatewayTest;
import com.fiap.tech.challenge.infrastructure.production.persistence.ItemJpaObject;
import com.fiap.tech.challenge.infrastructure.production.persistence.ProductionJpaEntity;
import com.fiap.tech.challenge.infrastructure.production.persistence.ProductionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
class ProductionPostgresGatewayTest {

    @Autowired
    private ProductionPostgresGateway productionGateway;

    @Autowired
    private ProductionRepository productionRepository;

    @Test
    void givenAValidProduction_whenCallsCreate_shouldReturnANewProduction() {
        final var expectedOrderId = UUID.randomUUID().toString();
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);

        final var expectedItems = List.of(expectedItem);

        final var aProduction = Production.createProduction(expectedOrderId, expectedItems);

        assertEquals(0, productionRepository.count());

        final var actualProduction = productionGateway.create(aProduction);

        assertEquals(1, productionRepository.count());
        assertEquals(expectedOrderId, actualProduction.getOrderId());
        assertEquals(expectedItems, actualProduction.getItems());
        assertEquals(aProduction.getStatus(), actualProduction.getStatus());
        assertEquals(aProduction.getStartedAt(), actualProduction.getStartedAt());
        assertEquals(aProduction.getFinishedAt(), actualProduction.getFinishedAt());

        final var actualEntity = productionRepository.findById(actualProduction.getId().getValue()).get();

        assertEquals(expectedOrderId, actualEntity.getOrderId());
        assertTrue(expectedItems.containsAll(actualEntity.getItems().stream().map(ItemJpaObject::toValueObject).toList()));
        assertEquals(aProduction.getStatus(), actualEntity.getStatus());
        assertEquals(aProduction.getStartedAt(), actualEntity.getStartedAt());
        assertEquals(aProduction.getFinishedAt(), actualEntity.getFinishedAt());
    }

    @Test
    @Transactional
    void givenAValidProduction_whenCallsUpdate_shouldReturnAUpdatedProduction() {
        final var expectedStatus = ProductionStatus.IN_PREPARATION;
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);
        final var expectedItems = List.of(expectedItem);
        final var production = productionGateway
                .create(Production.createProduction(UUID.randomUUID().toString(),
                        expectedItems));

        final var aProduction = Production.with(production).updateStatus(expectedStatus);

        assertEquals(1, productionRepository.count());

        final var actualProduction = productionGateway.update(aProduction);

        assertEquals(1, productionRepository.count());
        assertEquals(production.getOrderId(), actualProduction.getOrderId());
        assertTrue(expectedItems.containsAll(actualProduction.getItems()));
        assertEquals(aProduction.getStatus(), actualProduction.getStatus());
        assertEquals(aProduction.getStartedAt(), actualProduction.getStartedAt());
        assertEquals(aProduction.getFinishedAt(), actualProduction.getFinishedAt());

        final var actualEntity = productionRepository.findById(actualProduction.getId().getValue()).get();

        assertEquals(production.getOrderId(), actualEntity.getOrderId());
        assertTrue(expectedItems.containsAll(actualEntity.getItems().stream().map(ItemJpaObject::toValueObject).toList()));
        assertEquals(aProduction.getStatus(), actualEntity.getStatus());
        assertEquals(aProduction.getStartedAt(), actualEntity.getStartedAt());
        assertEquals(aProduction.getFinishedAt(), actualEntity.getFinishedAt());
    }

    @Test
    @Transactional
    void givenAValidProductionId_whenCallsFindById_shouldReturnAProduction() {
        final var expectedItem = Item.of(UUID.randomUUID().toString(), "productName", 2);
        final var expectedItems = List.of(expectedItem);
        final var production = productionGateway
                .create(Production.createProduction(UUID.randomUUID().toString(),
                        expectedItems));

        final var aProduction = Production.with(production);
        final var expectedId = aProduction.getId();
        assertEquals(1, productionRepository.count());

        final var actualProduction = productionGateway.findById(expectedId).get();

        assertEquals(1, productionRepository.count());
        assertEquals(production.getOrderId(), actualProduction.getOrderId());
        assertTrue(expectedItems.containsAll(actualProduction.getItems()));
        assertEquals(aProduction.getStatus(), actualProduction.getStatus());
        assertEquals(aProduction.getStartedAt(), actualProduction.getStartedAt());
        assertEquals(aProduction.getFinishedAt(), actualProduction.getFinishedAt());

        final var actualEntity = productionRepository.findById(actualProduction.getId().getValue()).get();

        assertEquals(production.getOrderId(), actualEntity.getOrderId());
        assertTrue(expectedItems.containsAll(actualEntity.getItems().stream().map(ItemJpaObject::toValueObject).toList()));
        assertEquals(aProduction.getStatus(), actualEntity.getStatus());
        assertEquals(aProduction.getStartedAt(), actualEntity.getStartedAt());
        assertEquals(aProduction.getFinishedAt(), actualEntity.getFinishedAt());
    }

    @Test
    @Transactional
    void givenAllParams_whenCallsFindAll_shouldReturnFilteredList() {
        List.of(
                productionGateway
                        .create(Production.createProduction(UUID.randomUUID().toString(),
                                List.of(Item.of(UUID.randomUUID().toString(), "productName", 2)))),
                productionGateway
                        .create(Production.createProduction(UUID.randomUUID().toString(),
                                List.of(Item.of(UUID.randomUUID().toString(), "productName", 2))))
        );

        assertEquals(2, productionRepository.count());
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "cei";
        final var expectedSort = "receivedAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery =
                new SearchQuery(
                        expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection
                );
        final var actualPage = productionGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
        assertEquals("RECEIVED", actualPage.items().get(0).getStatus().name());
    }
}