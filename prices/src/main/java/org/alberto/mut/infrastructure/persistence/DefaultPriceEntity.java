package org.alberto.mut.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.alberto.mut.domain.models.Price;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "PRODUCT_DEFAULT_PRICES")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultPriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PRODUCT_ID")
    private Integer productId;

    @Column(name = "BRAND_ID")
    private Integer brandId;

    @Column(name = "DEFAULT_PRICE")
    private Double defaultPrice;

    public Price toDomain() {
        return new Price(productId, brandId, defaultPrice);
    }
}