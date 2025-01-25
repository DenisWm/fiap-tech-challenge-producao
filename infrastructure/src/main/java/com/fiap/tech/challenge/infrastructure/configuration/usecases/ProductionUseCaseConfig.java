package com.fiap.tech.challenge.infrastructure.configuration.usecases;

import com.fiap.tech.challenge.application.production.create.CreateProductionUseCase;
import com.fiap.tech.challenge.application.production.create.DefaultCreateProductionUseCase;
import com.fiap.tech.challenge.application.production.retrieve.get.DefaultGetProductionByIdUseCase;
import com.fiap.tech.challenge.application.production.retrieve.get.GetProductionByIdUseCase;
import com.fiap.tech.challenge.application.production.retrieve.list.DefaultListProductionUseCase;
import com.fiap.tech.challenge.application.production.retrieve.list.ListProductionUseCase;
import com.fiap.tech.challenge.application.production.update.DefaultUpdateProductionStatusUseCase;
import com.fiap.tech.challenge.application.production.update.UpdateProductionStatusUseCase;
import com.fiap.tech.challenge.domain.production.ProductionGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class ProductionUseCaseConfig {

    private final ProductionGateway productionGateway;

    public ProductionUseCaseConfig(final ProductionGateway productionGateway) {
        this.productionGateway = Objects.requireNonNull(productionGateway);
    }

    @Bean
    public GetProductionByIdUseCase getProductionByIdUseCase() {
        return new DefaultGetProductionByIdUseCase(productionGateway);
    }

    @Bean
    public UpdateProductionStatusUseCase getUpdateProductionStatusCommand() {
        return new DefaultUpdateProductionStatusUseCase(productionGateway);
    }

    @Bean
    public CreateProductionUseCase createProductionUseCase() {
        return new DefaultCreateProductionUseCase(productionGateway);
    }

    @Bean
    public ListProductionUseCase listProductionUseCase() {
        return new DefaultListProductionUseCase(productionGateway);
    }
}
