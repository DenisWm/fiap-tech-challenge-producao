package com.fiap.tech.challenge.infrastructure.production.persistence;

import com.fiap.tech.challenge.domain.production.Production;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionRepository extends JpaRepository<ProductionJpaEntity, String> {

    Page<ProductionJpaEntity> findAll(Specification<ProductionJpaEntity> whereClause, Pageable page);

}
