package com.fiap.tech.challenge.application.production.retrieve.list;

import com.fiap.tech.challenge.application.UseCase;
import com.fiap.tech.challenge.domain.pagination.Pagination;
import com.fiap.tech.challenge.domain.pagination.SearchQuery;

public abstract class ListProductionUseCase extends UseCase<SearchQuery, Pagination<ProductionListOutput>> {
}
