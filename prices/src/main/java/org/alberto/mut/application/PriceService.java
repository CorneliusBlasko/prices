package org.alberto.mut.application;

import org.alberto.mut.domain.models.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceService {
    Optional<Price> getApplicablePrice(LocalDateTime applicationDate, Integer productId, Integer brandId);
}
