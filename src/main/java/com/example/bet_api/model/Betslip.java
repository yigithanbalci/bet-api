package com.example.bet_api.model;

import com.example.bet_api.dto.BetType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;

import java.math.BigDecimal;

@Data
@Builder
@Entity
@Table(name = "betslips")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false) // to flag this intentional
public class Betslip extends BaseEntity {

    @Id
    //NOTE: Sequence is faster and more flexible
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bulletinSeqGen")
    @SequenceGenerator(name = "bulletinSeqGen", sequenceName = "bulletin_sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, updatable = false)
    @CreatedBy
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false, updatable = false)
    private Bulletin bulletin;

    @Enumerated(EnumType.STRING)
    @Column(name = "bet_type", nullable = false, length = 20)
    private BetType betType;

    @Column(name = "odds_realized", nullable = false, precision = 4, scale = 2)
    private BigDecimal oddsRealized;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "multiple_count", nullable = false)
    private Integer multipleCount;
}
