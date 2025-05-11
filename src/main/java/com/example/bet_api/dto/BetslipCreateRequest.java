package com.example.bet_api.dto;

import com.example.bet_api.model.Betslip;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Betslip create request")
public record BetslipCreateRequest(
        @NotNull @Min(value = 1)
        @Schema(description = "Event (Bulletin) Id for creating betslip", example = "1")
        Long eventId,
        @NotNull @Schema(description = "The bet type (home, draw or away)", example = "HOME")
        BetType betType,
        @NotNull @DecimalMin(value = "1.00") @DecimalMax(value = "10000.00")
        @Schema(description = "Amount to bet", example = "100.00")
        BigDecimal amount,
        @NotNull @Min(1) @Max(500)
        @Schema(description = "Number of multiple coupons", example = "3")
        Integer multipleCount
) {
    public static Betslip toEntity(BetslipCreateRequest request) {
        return Betslip.builder()
                .multipleCount(request.multipleCount())
                .betType(request.betType())
                .amount(request.amount()).build();
    }
}
