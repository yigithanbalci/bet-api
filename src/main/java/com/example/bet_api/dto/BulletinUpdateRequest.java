package com.example.bet_api.dto;

import java.math.BigDecimal;

public record BulletinUpdateRequest(
        Long eventId,
        BigDecimal oddsHomeWin,
        BigDecimal oddsDraw,
        BigDecimal oddsAwayWin
) {
}
