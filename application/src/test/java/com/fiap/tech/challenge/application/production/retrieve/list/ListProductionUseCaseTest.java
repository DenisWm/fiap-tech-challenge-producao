package com.fiap.tech.challenge.application.production.retrieve.list;

import com.fiap.tech.challenge.application.production.UseCaseTest;
import com.fiap.tech.challenge.domain.pagination.Pagination;
import com.fiap.tech.challenge.domain.pagination.SearchQuery;
import com.fiap.tech.challenge.domain.production.Item;
import com.fiap.tech.challenge.domain.production.Production;
import com.fiap.tech.challenge.domain.production.ProductionGateway;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListProductionUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListProductionUseCase useCase;

    @Mock
    private ProductionGateway productionGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(productionGateway);
    }

    @Test
    void givenEmptyResult_whenCallList_shouldReturnIt() {

        final var productions = List.<Production>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTotal = 0;
        final var expectedSort = "receivedAt";
        final var expectedDir = "desc";
        final var expectedTerms = "";
        final var expectedItems = List.of();

        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, expectedTotal, productions);

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDir);

        when(productionGateway.findAll(any())).thenReturn(expectedPagination);

        final var aResult = useCase.execute(aQuery);

        assertEquals(expectedPage, aResult.currentPage());
        assertEquals(expectedPerPage, aResult.perPage());
        assertEquals(expectedTotal, aResult.total());
        assertEquals(expectedItems, aResult.items());

        verify(productionGateway).findAll(eq(aQuery));
    }
}