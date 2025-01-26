package com.fiap.tech.challenge.infrastructure.production;

import com.fiap.tech.challenge.domain.pagination.Pagination;
import com.fiap.tech.challenge.domain.pagination.SearchQuery;
import com.fiap.tech.challenge.domain.production.Production;
import com.fiap.tech.challenge.domain.production.ProductionGateway;
import com.fiap.tech.challenge.domain.production.ProductionID;
import com.fiap.tech.challenge.domain.production.ProductionStatus;
import com.fiap.tech.challenge.infrastructure.production.persistence.ProductionJpaEntity;
import com.fiap.tech.challenge.infrastructure.production.persistence.ProductionRepository;
import com.fiap.tech.challenge.infrastructure.services.EventService;
import com.fiap.tech.challenge.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Component
public class ProductionPostgresGateway implements ProductionGateway {

    private final ProductionRepository productionRepository;

    private final EventService eventService;

    public ProductionPostgresGateway(
            final ProductionRepository productionRepository,
            final EventService eventService
    ) {
        this.productionRepository = Objects.requireNonNull(productionRepository);
        this.eventService = eventService;
    }

    @Override
    @Transactional
    public Production create(final Production aProduction) {
        return save(aProduction);
    }

    private Production save(final Production aProduction) {
        final var result = this.productionRepository.save(ProductionJpaEntity.from(aProduction)).toAggregate();
        aProduction.publishDomainEvents(eventService::send);
        return result;
    }

    @Override
    @Transactional
    public Production update(final Production aProduction) {
        return save(aProduction);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Production> findById(final ProductionID anId) {
        return this.productionRepository.findById(anId.getValue()).map(ProductionJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Production> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var where = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .map(spec -> spec.and(excludeReady()))
                .orElse(excludeReady());

        final var result = productionRepository.findAll(Specification.where(where), page);

        return new Pagination<>(
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.map(ProductionJpaEntity::toAggregate).stream().toList()
        );
    }

    private Specification<ProductionJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("status", terms);
    }

    private Specification<ProductionJpaEntity> excludeReady() {
        return SpecificationUtils.notEqual("status", ProductionStatus.READY);
    }
}
