package com.fiap.tech.challenge.domain.production;

import com.fiap.tech.challenge.domain.pagination.Pagination;
import com.fiap.tech.challenge.domain.pagination.SearchQuery;

import java.util.Optional;

public interface ProductionGateway {

    Production create(Production aProduction);

    Production update(Production aProduction);

    Optional<Production> findById(ProductionID anId);

    Pagination<Production> findAll(SearchQuery aQuery);
}
