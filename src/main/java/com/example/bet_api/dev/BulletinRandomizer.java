package com.example.bet_api.dev;

import com.example.bet_api.repository.BulletinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class BulletinRandomizer {
    private final BulletinRepository bulletinRepository;

    //    @Scheduled(fixedRate = 1000)
    public void run() {
        Thread.ofVirtual().start(() -> {
            bulletinRepository.saveAll(
                    bulletinRepository.findAll().stream().peek(bulletin -> {
                        bulletin.setOddsHomeWin(getRand());
                        bulletin.setOddsDraw(getRand());
                        bulletin.setOddsAwayWin(getRand());
                    }).collect(Collectors.toList()));
        });
    }

    protected BigDecimal getRand() {
        return this.getRand(1, 10);
    }

    protected BigDecimal getRand(double min, double max) {
        double random = ThreadLocalRandom.current().nextDouble(min, max);
        return new BigDecimal(random).setScale(2, RoundingMode.HALF_UP);
    }
}
