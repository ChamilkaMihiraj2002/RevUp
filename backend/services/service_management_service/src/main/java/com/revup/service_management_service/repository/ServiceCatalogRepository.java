package com.revup.service_management_service.repository;

import com.revup.service_management_service.model.ServiceCatalog;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ServiceCatalogRepository extends R2dbcRepository<ServiceCatalog, Long> {

    Flux<ServiceCatalog> findByIsActive(Boolean isActive);

    Flux<ServiceCatalog> findByCategoryContainingIgnoreCase(String category);

    Flux<ServiceCatalog> findByNameContainingIgnoreCase(String name);

    @Query("SELECT * FROM service_schema.service_catalog WHERE is_active = true ORDER BY name")
    Flux<ServiceCatalog> findAllActiveServices();

    @Query("SELECT * FROM service_schema.service_catalog " +
           "WHERE category = :category AND is_active = true " +
           "ORDER BY base_price")
    Flux<ServiceCatalog> findActiveByCategoryOrderByPrice(String category);

    Mono<Boolean> existsByName(String name);
}
