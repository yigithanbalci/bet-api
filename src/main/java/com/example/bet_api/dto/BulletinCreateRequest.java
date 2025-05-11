package com.example.bet_api.dto;

import com.example.bet_api.model.Bulletin;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record BulletinCreateRequest(
        @NotBlank String leagueName,
        @NotBlank String homeTeam,
        @NotBlank String awayTeam,
        @NotNull @DecimalMin(value = "1.00") BigDecimal oddsHomeWin,
        @NotNull @DecimalMin(value = "1.00") BigDecimal oddsDraw,
        @NotNull @DecimalMin(value = "1.00") BigDecimal oddsAwayWin,
        @NotNull @Future Instant startTimeUtc
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
