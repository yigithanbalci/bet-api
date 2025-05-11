package com.example.bet_api.dto;

import com.example.bet_api.model.Betslip;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Betslip response")
public record BetslipResponse(
        @NotNull @Min(value = 1)
        @Schema(description = "Betslip Id", example = "1")
        Long id,
        @NotNull @Min(value = 1)
        @Schema(description = "Customer Id that owns betslip", example = "1")
        Long customerId,
        @NotNull @Min(value = 1)
        @Schema(description = "Event (Bulletin) Id for creating betslip", example = "1")
        Long eventId,
        @NotBlank @Schema(description = "The bet type (home, draw or away)", example = "HOME")
        BetType betType,
        @NotNull @DecimalMin(value = "1.00") @DecimalMax(value = "99.00")
        @Schema(description = "Odds requested", example = "1.50")
        BigDecimal oddsRequested,
        @NotNull @DecimalMin(value = "1.00") @DecimalMax(value = "99.00")
        @Schema(description = "Odds requested", example = "1.52")
        BigDecimal oddsRealized,
        @NotNull @DecimalMin(value = "1.00") @DecimalMax(value = "10000.00")
        @Schema(description = "Amount to bet", example = "100.00")
        BigDecimal amount,
        @NotNull @Min(1) @Max(500)
        @Schema(description = "Number of multiple coupons", example = "3")
        Integer multipleCount
) {
    public static BetslipResponse toResponse(Betslip betslip, BigDecimal oddsRequested) {
        return new BetslipResponse(
                betslip.getId(),
                betslip.getCustomer().getId(),
                betslip.getBulletin().getEventId(),
                betslip.getBetType(),
                oddsRequested,
                betslip.getOddsRealized(),
                betslip.getAmount(),
                betslip.getMultipleCount());
    }
}
