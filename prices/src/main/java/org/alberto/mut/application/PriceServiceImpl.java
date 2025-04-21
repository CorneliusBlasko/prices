package org.alberto.mut.application;

import org.alberto.mut.domain.models.Price;
import org.alberto.mut.domain.port.out.PriceRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepositoryPort priceRepositoryPort;

    public PriceServiceImpl(PriceRepositoryPort priceRepositoryPort) {
        this.priceRepositoryPort = priceRepositoryPort;
    }

    @Override
    public Optional<Price> getApplicablePrice(LocalDateTime applicationDate, Integer productId, Integer brandId) {
        Optional<Price> applicablePrice = priceRepositoryPort.findApplicablePrices(applicationDate, productId, brandId)
                .stream()
                .filter(price ->
                        !applicationDate.isBefore(price.startDate()) &&
                                !applicationDate.isAfter(price.endDate())
                )
                .max(Comparator.comparingInt(Price::priority));

        if (applicablePrice.isPresent()) {
            return applicablePrice;
        } else {
            return priceRepositoryPort.findDefaultPrice(productId, brandId);
        }
    }
}
