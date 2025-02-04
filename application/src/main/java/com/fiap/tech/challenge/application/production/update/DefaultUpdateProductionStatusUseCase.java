package com.fiap.tech.challenge.application.production.update;

import com.fiap.tech.challenge.domain.exceptions.NotFoundException;
import com.fiap.tech.challenge.domain.exceptions.NotificationException;
import com.fiap.tech.challenge.domain.production.Production;
import com.fiap.tech.challenge.domain.production.ProductionGateway;
import com.fiap.tech.challenge.domain.production.ProductionID;
import com.fiap.tech.challenge.domain.production.ProductionStatus;
import com.fiap.tech.challenge.domain.validation.Error;
import com.fiap.tech.challenge.domain.validation.handler.Notification;

import java.util.Objects;

public class DefaultUpdateProductionStatusUseCase extends UpdateProductionStatusUseCase {

    private final ProductionGateway productionGateway;

    public DefaultUpdateProductionStatusUseCase(final ProductionGateway productionGateway) {
        this.productionGateway = Objects.requireNonNull(productionGateway);
    }

    @Override
    public UpdateProductionStatusOutput execute(final UpdateProductionStatusCommand aCmd) {
        final var anId = ProductionID.from(aCmd.id());
        final var status = ProductionStatus.of(aCmd.status()).orElse(null);

        final var aProduction = productionGateway.findById(anId)
                .orElseThrow(() -> NotFoundException.with(Production.class, anId));

        final var notification = validateProductionStatus(aProduction, status);

        aProduction.updateStatus(status);

        if(notification.hasErrors()) {
            throw new NotificationException("Could not update Aggregate Production", notification);
        }

        return UpdateProductionStatusOutput.from(productionGateway.update(aProduction));
    }

    private Notification validateProductionStatus(final Production aProduction, final ProductionStatus newStatus) {
        final var notification = Notification.create();
        if (newStatus == null) {
            notification.append(new Error("'status' must not be null"));
            return notification;
        } else {
            switch (aProduction.getStatus()) {
                case RECEIVED:
                    if (newStatus != ProductionStatus.IN_PREPARATION) {
                        notification.append(new Error(
                                "Cannot transition from RECEIVED to " + newStatus.name()));
                    }
                    break;

                case IN_PREPARATION:
                    if (newStatus != ProductionStatus.READY) {
                        notification.append(new Error(
                                "Cannot transition from IN_PREPARATION to " + newStatus.name()));
                    }
                    break;

                case READY:
                    notification.append(new Error(
                            "Cannot transition from READY to any other status"));
                    break;
            }
        }
        return notification;
    }
}
