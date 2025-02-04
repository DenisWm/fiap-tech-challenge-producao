package com.fiap.tech.challenge.application.production.retrieve.list;

import com.fiap.tech.challenge.domain.pagination.Pagination;
import com.fiap.tech.challenge.domain.pagination.SearchQuery;
import com.fiap.tech.challenge.domain.production.ProductionGateway;

import java.util.Objects;

public class DefaultListProductionUseCase extends ListProductionUseCase{

    private final ProductionGateway productionGateway;

    public DefaultListProductionUseCase(final ProductionGateway productionGateway) {
        this.productionGateway = Objects.requireNonNull(productionGateway);
    }

    @Override
    public Pagination<ProductionListOutput> execute(final SearchQuery aQuery) {
        return productionGateway.findAll(aQuery)
                .map(ProductionListOutput::from);
    }
}
