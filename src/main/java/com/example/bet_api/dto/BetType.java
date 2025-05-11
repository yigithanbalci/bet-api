package com.example.bet_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Type of bet: HOME for home win, AWAY for away win, DRAW for a draw")
public enum BetType {

    @Schema(description = "Home team wins")
    HOME,

    @Schema(description = "Away team wins")
    AWAY,

    @Schema(description = "The match ends in a draw")
    DRAW
}
