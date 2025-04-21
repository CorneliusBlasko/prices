package org.alberto.mut.domain.port.out;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.alberto.mut.domain.models.Price;

public interface PriceRepositoryPort {
    List<Price> findApplicablePrices(LocalDateTime applicationDate, Integer productId, Integer brandId);
    Optional<Price> findDefaultPrice(Integer productId, Integer brandId);
}
