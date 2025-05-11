package com.example.bet_api.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Audited
@Builder
@Entity
@Table(name = "bulletins")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false) // to flag this intentional
public class Bulletin extends BaseEntity {

    @Id
    //NOTE: Sequence is faster and more flexible
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bulletinSeqGen")
    @SequenceGenerator(name = "bulletinSeqGen", sequenceName = "bulletin_sequence")
    private Long eventId;

    @Column(name = "league_name", nullable = false)
    // TODO: yigithanbalci 11.05.2025: add manyToOne relation to league_names later for validity
    private String leagueName;

    @Column(name = "home_team", nullable = false)
    // TODO: yigithanbalci 11.05.2025: add manyToOne relation to team_names later for validity
    private String homeTeam;

    @Column(name = "away_team", nullable = false)
    // TODO: yigithanbalci 11.05.2025: add manyToOne relation to team_names later for validity
    private String awayTeam;

    @Column(name = "odds_home_win", nullable = false, precision = 4, scale = 2)
    private BigDecimal oddsHomeWin;

    @Column(name = "odds_draw", nullable = false, precision = 4, scale = 2)
    private BigDecimal oddsDraw;

    @Column(name = "odds_away_win", nullable = false, precision = 4, scale = 2)
    private BigDecimal oddsAwayWin;

    @Column(name = "start_time_utc", nullable = false)
    private Instant startTimeUtc;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}
