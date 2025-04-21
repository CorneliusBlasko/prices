package org.alberto.mut.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DefaultPriceJpaRepository extends JpaRepository<DefaultPriceEntity, Long> {
    Optional<DefaultPriceEntity> findByProductIdAndBrandId(Integer productId, Integer brandId);
}