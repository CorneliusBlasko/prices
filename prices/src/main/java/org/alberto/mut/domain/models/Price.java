package org.alberto.mut.domain.models;

import java.time.LocalDateTime;

public record Price(
        Long id,
        Integer brandId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer priceList,
        Integer productId,
        Integer priority,
        Double price,
        String currency,
        Double defaultPrice
) {
    public boolean isApplicable(LocalDateTime dateTime) {
         return !dateTime.isBefore(startDate) && !dateTime.isAfter(endDate);
     }

    public Price(Long id, Integer brandId, LocalDateTime startDate, LocalDateTime endDate, Integer priceList, Integer productId, Integer priority, Double price, String currency) {
        this(id, brandId, startDate, endDate, priceList, productId, priority, price, currency, null);
    }

     public Price(Integer productId, Integer brandId, Double defaultPrice) {
        this(null, brandId, null, null, null, productId, -1, null, "EUR", defaultPrice);
    }
}