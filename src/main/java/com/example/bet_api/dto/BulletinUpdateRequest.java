package com.example.bet_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Bulletin update request")
public record BulletinUpdateRequest(
        @NotNull @Min(value = 1)
        @Schema(description = "Event (Bulletin) Id for the update", example = "1")
        Long eventId,
        @NotNull @DecimalMin(value = "1.00")
        @Schema(description = "Odds ratio of home team to win", example = "1.52") BigDecimal oddsHomeWin,
        @NotNull @DecimalMin(value = "1.00")
        @Schema(description = "Odds ratio of draw", example = "1.22") BigDecimal oddsDraw,
        @NotNull @DecimalMin(value = "1.00")
        @Schema(description = "Odds ratio of away team to win", example = "1.42") BigDecimal oddsAwayWin
) {
}
