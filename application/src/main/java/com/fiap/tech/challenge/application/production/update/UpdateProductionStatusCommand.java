package com.fiap.tech.challenge.application.production.update;

import com.fiap.tech.challenge.domain.production.ProductionID;
import com.fiap.tech.challenge.domain.production.ProductionStatus;

public record UpdateProductionStatusCommand(
        String id,
        String status
) {

    public static UpdateProductionStatusCommand with(final String id, final String status) {
        return new UpdateProductionStatusCommand(id, status);
    }
}
