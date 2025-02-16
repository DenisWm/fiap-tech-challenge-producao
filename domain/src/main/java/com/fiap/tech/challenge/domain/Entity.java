package com.fiap.tech.challenge.domain;

import com.fiap.tech.challenge.domain.events.DomainEvent;
import com.fiap.tech.challenge.domain.events.DomainEventPublisher;
import com.fiap.tech.challenge.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Entity<ID extends Identifier> extends ValueObject{

    protected final ID id;
    private final List<DomainEvent> domainEvents;

    protected Entity(final ID id, final List<DomainEvent> domainEvents) {
        this.domainEvents = new ArrayList<>(domainEvents == null ? Collections.emptyList() : domainEvents);
        Objects.requireNonNull(id, "'id' should not be null");
        this.id = id;
    }

    public abstract void validate(ValidationHandler handler);

    public ID getId() {
        return id;
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void publishDomainEvents(final DomainEventPublisher publisher) {
        if(publisher == null) {
            return;
        }
        getDomainEvents().forEach(publisher::publish);

        this.domainEvents.clear();
    }

    public void registerEvent(final DomainEvent event) {
        if(event != null) {
            this.domainEvents.add(event);
        }
    }
}
