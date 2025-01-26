package com.fiap.tech.challenge.infrastructure.services.local;

import com.fiap.tech.challenge.infrastructure.configuration.Json;
import com.fiap.tech.challenge.infrastructure.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryEventService implements EventService {

    private final Logger logger = LoggerFactory.getLogger(InMemoryEventService.class);

    @Override
    public void send(final Object event) {
        logger.info("Event was observed: {}", Json.writeValueAsString(event));
    }
}
