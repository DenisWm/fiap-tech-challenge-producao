package com.fiap.tech.challenge.infrastructure.production;

import com.fiap.tech.challenge.infrastructure.PostgresGatewayTest;
import com.fiap.tech.challenge.infrastructure.production.persistence.ProductionRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@PostgresGatewayTest
class ProductionPostgresGatewayTest  {

    @Autowired
    private ProductionPostgresGateway productionGateway;

    @Autowired
    private ProductionRepository productionRepository;


}