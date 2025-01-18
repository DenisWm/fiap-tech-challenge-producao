package com.fiap.tech.challenge.domain.events;

@FunctionalInterface
public interface DomainEventPublisher {

    void publish(DomainEvent event);
}
