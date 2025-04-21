package org.alberto.mut.infrastructure.api.dto;

import org.alberto.mut.domain.models.Price;

import java.time.LocalDateTime;

public record PriceResponseDTO(
        Integer productId,
        Integer brandId,
        Integer priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Double price,
        Double defaultPrice
) {
    public static PriceResponseDTO from(Price price) {
        return new PriceResponseDTO(
                price.productId(),
                price.brandId(),
                price.priceList(),
                price.startDate(),
                price.endDate(),
                price.price(),
                price.defaultPrice()
        );
    }
}