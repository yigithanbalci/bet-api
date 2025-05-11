package com.example.bet_api.dto;

import com.example.bet_api.model.Bulletin;

import java.math.BigDecimal;
import java.time.Instant;

public record BulletinResponse(
        Long eventId,
        String leagueName,
        String homeTeam,
        String awayTeam,
        BigDecimal oddsHomeWin,
        BigDecimal oddsDraw,
        BigDecimal oddsAwayWin,
        Instant startTimeUtc
) {
    public static BulletinResponse toResponse(Bulletin bulletin) {
        return new BulletinResponse(
                bulletin.getEventId(),
                bulletin.getLeagueName(),
                bulletin.getHomeTeam(),
                bulletin.getAwayTeam(),
                bulletin.getOddsHomeWin(),
                bulletin.getOddsDraw(),
                bulletin.getOddsAwayWin(),
                bulletin.getStartTimeUtc()
        );
    }
}
