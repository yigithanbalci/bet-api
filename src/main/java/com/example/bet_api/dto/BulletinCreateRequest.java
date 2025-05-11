package com.example.bet_api.dto;

import com.example.bet_api.model.Bulletin;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Bulletin create request")
public record BulletinCreateRequest(
        @NotBlank @Schema(description = "The name of the league that match is going to be played", example = "Birinci Lig")
        String leagueName,
        @NotBlank @Schema(description = "The name of the home team", example = "Liverpool")
        String homeTeam,
        @NotBlank @Schema(description = "The name of the away team", example = "Barcelona")
        String awayTeam,
        @NotNull @DecimalMin(value = "1.00")
        @Schema(description = "Odds ratio of home team to win", example = "1.52") BigDecimal oddsHomeWin,
        @NotNull @DecimalMin(value = "1.00")
        @Schema(description = "Odds ratio of draw", example = "1.22") BigDecimal oddsDraw,
        @NotNull @DecimalMin(value = "1.00")
        @Schema(description = "Odds ratio of away team to win", example = "1.42") BigDecimal oddsAwayWin,
        @NotNull @Future
        @Schema(description = "Start time of the match in UTC time", example = "2025-05-12T15:00:00Z") Instant startTimeUtc
) {
    public static Bulletin toEntity(BulletinCreateRequest request) {
        return Bulletin.builder()
                .leagueName(request.leagueName())
                .homeTeam(request.homeTeam())
                .awayTeam(request.awayTeam())
                .oddsHomeWin(request.oddsHomeWin())
                .oddsDraw(request.oddsDraw())
                .oddsAwayWin(request.oddsAwayWin())
                .startTimeUtc(request.startTimeUtc()).build();
    }
}
