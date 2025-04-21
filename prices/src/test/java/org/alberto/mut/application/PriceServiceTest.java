package org.alberto.mut.application;

import org.alberto.mut.domain.models.Price;
import org.alberto.mut.domain.port.out.PriceRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class PriceServiceTest {

    private PriceService priceService;

    @Mock
    private PriceRepositoryPort priceRepositoryPort;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        priceService = new PriceServiceImpl(priceRepositoryPort);
    }

    // Tests the scenario where a price is found within the specified date range
    @Test
    void testGetApplicablePrice_withinRange() {
        // Arrange
        LocalDateTime applicationDate = LocalDateTime.parse("2020-06-14-16.00.00", formatter);
        Integer productId = 35455;
        Integer brandId = 1;
        Price expectedPrice = new Price(1L, brandId, LocalDateTime.parse("2020-06-14-15.00.00", formatter), LocalDateTime.parse("2020-06-14-18.30.00", formatter), 2, productId, 1, 25.45, "EUR", 0.0);
        when(priceRepositoryPort.findApplicablePrices(applicationDate, productId, brandId))
                .thenReturn(List.of(
                        new Price(1L, brandId, LocalDateTime.parse("2020-06-14-00.00.00", formatter), LocalDateTime.parse("2020-12-31-23.59.59", formatter), 1, productId, 0, 35.50, "EUR", 0.0),
                        expectedPrice
                ));

        // Act
        Optional<Price> actualPrice = priceService.getApplicablePrice(applicationDate, productId, brandId);

        // Assert
        assertTrue(actualPrice.isPresent());
        assertEquals(expectedPrice, actualPrice.get());
    }

    //  Tests the scenario where the application date is at the start of a price's date range
    @Test
    void testGetApplicablePrice_edgeOfRange() {
        // Arrange
        LocalDateTime applicationDate = LocalDateTime.parse("2020-06-14-15.00.00", formatter);
        Integer productId = 35455;
        Integer brandId = 1;
        Price expectedPrice = new Price(1L, brandId, applicationDate, LocalDateTime.parse("2020-06-14-18.30.00", formatter), 2, productId, 1, 25.45, "EUR", 0.0);
        when(priceRepositoryPort.findApplicablePrices(applicationDate, productId, brandId))
                .thenReturn(List.of(expectedPrice));

        // Act
        Optional<Price> actualPrice = priceService.getApplicablePrice(applicationDate, productId, brandId);

        // Assert
        assertTrue(actualPrice.isPresent());
        assertEquals(expectedPrice, actualPrice.get());
    }

    // Tests the scenario where no price is found within the specified date range, and no default price exist
    @Test
    void testGetApplicablePrice_noRangeMatch() {
        // Arrange
        LocalDateTime applicationDate = LocalDateTime.parse("2025-01-01-00.00.00", formatter);
        Integer productId = 35455;
        Integer brandId = 1;
        when(priceRepositoryPort.findApplicablePrices(applicationDate, productId, brandId))
                .thenReturn(Collections.emptyList());
        when(priceRepositoryPort.findDefaultPrice(productId, brandId))
                .thenReturn(Optional.empty());

        // Act
        Optional<Price> actualPrice = priceService.getApplicablePrice(applicationDate, productId, brandId);

        // Assert
        assertTrue(actualPrice.isEmpty());
    }

    // Tests the scenario where no price is found within the date range, but a default price is available
    @Test
    void testGetApplicablePrice_defaultPrice() {
        // Arrange
        LocalDateTime applicationDate = LocalDateTime.parse("2025-01-01-00.00.00", formatter);
        Integer productId = 35455;
        Integer brandId = 1;
        Price expectedPrice = new Price(productId, brandId, 40.00);
        when(priceRepositoryPort.findApplicablePrices(applicationDate, productId, brandId))
                .thenReturn(Collections.emptyList());
        when(priceRepositoryPort.findDefaultPrice(productId, brandId))
                .thenReturn(Optional.of(expectedPrice));

        // Act
        Optional<Price> actualPrice = priceService.getApplicablePrice(applicationDate, productId, brandId);

        // Assert
        assertTrue(actualPrice.isPresent());
        assertEquals(expectedPrice, actualPrice.get());
    }

    // Tests the scenario where multiple prices are found within the date range, and the price with the highest priority is returned
    @Test
    void testGetApplicablePrice_multiplePrices_highestPriority() {
        // Arrange
        LocalDateTime applicationDate = LocalDateTime.parse("2020-06-14-16.00.00", formatter);
        Integer productId = 35455;
        Integer brandId = 1;
        Price expectedPrice = new Price(2L, brandId, LocalDateTime.parse("2020-06-14-15.00.00", formatter), LocalDateTime.parse("2020-06-14-18.30.00", formatter), 2, productId, 2, 28.00, "EUR", 0.0); // Higher priority
        when(priceRepositoryPort.findApplicablePrices(applicationDate, productId, brandId))
                .thenReturn(List.of(
                        new Price(1L, brandId, LocalDateTime.parse("2020-06-14-00.00.00", formatter), LocalDateTime.parse("2020-12-31-23.59.59", formatter), 1, productId, 1, 35.50, "EUR", 0.0),
                        expectedPrice,
                        new Price(3L, brandId, LocalDateTime.parse("2020-06-14-15.00.00", formatter), LocalDateTime.parse("2020-06-14-18.30.00", formatter), 3, productId, 0, 25.45, "EUR", 0.0)
                ));

        // Act
        Optional<Price> actualPrice = priceService.getApplicablePrice(applicationDate, productId, brandId);

        // Assert
        assertTrue(actualPrice.isPresent());
        assertEquals(expectedPrice, actualPrice.get());
    }
}
