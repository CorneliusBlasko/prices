package org.alberto.mut.infrastructure.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.alberto.mut.application.PriceService;
import org.alberto.mut.domain.models.Price;
import org.alberto.mut.infrastructure.api.dto.PriceResponseDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@Tag(name = "Prices", description = "Products' applicable prices operations")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Checks if the service is alive.")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public ResponseEntity<String> getHealth() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/prices")
    @Operation(
            summary = "Find the applicable price for a product at a given date and brand",
            description = "Retrieves the price information based on the application date, product ID, and brand ID. " +
                    " If no specific price is found for the date provided, the default price is returned."
    )
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PriceResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    @ApiResponse(responseCode = "404", description = "No applicable price found")
    public ResponseEntity<PriceResponseDTO> getApplicablePrice(
            @Parameter(description = "Date and time of application (e.g., 2020-06-14T10:00:00)", required = true)
            @RequestParam("application_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @Parameter(description = "Identifier of the product", required = true)
            @RequestParam("product_id") Integer productId,
            @Parameter(description = "Identifier of the brand (e.g., 1 for ZARA)", required = true)
            @RequestParam("brand_id") Integer brandId) {

        Optional<Price> applicablePrice = priceService.getApplicablePrice(applicationDate, productId, brandId);

        return applicablePrice
                .map(PriceResponseDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
