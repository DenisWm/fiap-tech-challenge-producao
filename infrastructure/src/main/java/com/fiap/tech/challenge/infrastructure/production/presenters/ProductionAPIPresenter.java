package com.fiap.tech.challenge.infrastructure.production.presenters;

import com.fiap.tech.challenge.application.production.retrieve.get.ProductionOutput;
import com.fiap.tech.challenge.application.production.retrieve.list.ProductionListOutput;
import com.fiap.tech.challenge.infrastructure.production.models.ProductionListResponse;
import com.fiap.tech.challenge.infrastructure.production.models.ProductionResponse;

public interface ProductionAPIPresenter {

    static ProductionResponse present(final ProductionOutput output) {
        return ProductionResponse.from(output);
    }

    static ProductionListResponse present(final ProductionListOutput output) {
        return ProductionListResponse.from(output);
    }
}
