package com.fiap.tech.challenge.domain.production;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductionStatusTest {

    @Test
    public void givenProductionStatus_whenCallsMatching_thenReturnMatched() {
        final var expectedProductionStatus = ProductionStatus.READY;

        final var aResult = ProductionStatus.matching("ea").get(0);

        assertEquals(expectedProductionStatus, aResult);
    }

}