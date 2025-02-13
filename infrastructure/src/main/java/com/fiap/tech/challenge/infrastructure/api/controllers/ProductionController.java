package com.fiap.tech.challenge.infrastructure.api.controllers;

import com.fiap.tech.challenge.application.production.create.CreateProductionCommand;
import com.fiap.tech.challenge.application.production.create.CreateProductionUseCase;
import com.fiap.tech.challenge.application.production.retrieve.get.GetProductionByIdUseCase;
import com.fiap.tech.challenge.application.production.retrieve.list.ListProductionUseCase;
import com.fiap.tech.challenge.application.production.update.UpdateProductionStatusCommand;
import com.fiap.tech.challenge.application.production.update.UpdateProductionStatusUseCase;
import com.fiap.tech.challenge.domain.pagination.Pagination;
import com.fiap.tech.challenge.domain.pagination.SearchQuery;
import com.fiap.tech.challenge.domain.production.Item;
import com.fiap.tech.challenge.infrastructure.api.ProductionAPI;
import com.fiap.tech.challenge.infrastructure.production.models.CreateProductionRequest;
import com.fiap.tech.challenge.infrastructure.production.models.ProductionListResponse;
import com.fiap.tech.challenge.infrastructure.production.models.ProductionResponse;
import com.fiap.tech.challenge.infrastructure.production.models.UpdateProductionRequest;
import com.fiap.tech.challenge.infrastructure.production.presenters.ProductionAPIPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class ProductionController implements ProductionAPI {

    private final GetProductionByIdUseCase getProductionByIdUseCase;
    private final UpdateProductionStatusUseCase updateProductionStatusUseCase;
    private final ListProductionUseCase listProductionUseCase;

    public ProductionController(
            final GetProductionByIdUseCase getProductionByIdUseCase,
            final UpdateProductionStatusUseCase updateProductionStatusUseCase,
            final ListProductionUseCase listProductionUseCase
    ) {
        this.getProductionByIdUseCase = Objects.requireNonNull(getProductionByIdUseCase);
        this.updateProductionStatusUseCase = Objects.requireNonNull(updateProductionStatusUseCase);
        this.listProductionUseCase = Objects.requireNonNull(listProductionUseCase);
    }

    @Override
    public ProductionResponse getById(final String id) {
        return ProductionAPIPresenter.present(this.getProductionByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(
            final String id,
            final UpdateProductionRequest aRequest
    ) {
        final var aCmd = UpdateProductionStatusCommand.with(id, aRequest.status());
        final var output = updateProductionStatusUseCase.execute(aCmd);
        return ResponseEntity.ok()
                .header("Location", "/productions/%s".formatted(output.id()))
                .body(output);
    }

    @Override
    public Pagination<ProductionListResponse> list(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String dir
    ) {
        final var searchQuery = new SearchQuery(page, perPage, search, sort, dir);

        return this.listProductionUseCase.execute(searchQuery)
                .map(ProductionAPIPresenter::present);
    }
}
