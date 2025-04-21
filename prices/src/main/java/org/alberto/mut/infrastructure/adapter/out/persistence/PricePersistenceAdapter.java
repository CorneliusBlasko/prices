package org.alberto.mut.infrastructure.adapter.out.persistence;

import org.alberto.mut.domain.models.Price;
import org.alberto.mut.domain.port.out.PriceRepositoryPort;
import org.alberto.mut.infrastructure.persistence.DefaultPriceEntity;
import org.alberto.mut.infrastructure.persistence.DefaultPriceJpaRepository;
import org.alberto.mut.infrastructure.persistence.PriceEntity;
import org.alberto.mut.infrastructure.persistence.PriceJpaRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PricePersistenceAdapter implements PriceRepositoryPort {

    private final PriceJpaRepository priceJpaRepository;
    private final DefaultPriceJpaRepository defaultPriceJpaRepository;

    public PricePersistenceAdapter(PriceJpaRepository priceJpaRepository, DefaultPriceJpaRepository defaultPriceJpaRepository) {
        this.priceJpaRepository = priceJpaRepository;
        this.defaultPriceJpaRepository = defaultPriceJpaRepository;
    }

    @Override
    public List<Price> findApplicablePrices(LocalDateTime applicationDate, Integer productId, Integer brandId) {
        return priceJpaRepository.findByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        productId, brandId, applicationDate, applicationDate)
                .stream()
                .map(PriceEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Price> findDefaultPrice(Integer productId, Integer brandId) {
        return defaultPriceJpaRepository.findByProductIdAndBrandId(productId, brandId)
                .map(DefaultPriceEntity::toDomain);
    }
}