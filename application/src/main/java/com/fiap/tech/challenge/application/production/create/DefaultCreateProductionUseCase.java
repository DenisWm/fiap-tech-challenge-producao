package com.fiap.tech.challenge.application.production.create;

import com.fiap.tech.challenge.domain.exceptions.NotificationException;
import com.fiap.tech.challenge.domain.production.Production;
import com.fiap.tech.challenge.domain.production.ProductionGateway;
import com.fiap.tech.challenge.domain.validation.handler.Notification;

import java.util.Objects;

public class DefaultCreateProductionUseCase extends CreateProductionUseCase {

    private final ProductionGateway productionGateway;

    public DefaultCreateProductionUseCase(final ProductionGateway productionGateway) {
        this.productionGateway = Objects.requireNonNull(productionGateway);
    }

    @Override
    public CreateProductionOutput execute(final CreateProductionCommand aCmd) {
        final var aProduction = Production.createProduction(aCmd.orderId(), aCmd.items());

        final var notification = Notification.create();

        aProduction.validate(notification);

        if(notification.hasErrors()) {
            throw new NotificationException("Could not create Aggregate Production", notification);
        }

        return CreateProductionOutput.from(productionGateway.create(aProduction));
    }
}
