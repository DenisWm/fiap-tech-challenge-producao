package com.fiap.tech.challenge.infrastructure;

import com.fiap.tech.challenge.infrastructure.services.local.InMemoryEventService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = InMemoryEventService.class)
public class TestContextUp {

    @Test
    public void contextLoads() {}
}
