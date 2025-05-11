package com.example.bet_api.service.impl;

import com.example.bet_api.dto.BetType;
import com.example.bet_api.dto.BetslipCreateRequest;
import com.example.bet_api.dto.BetslipResponse;
import com.example.bet_api.model.Betslip;
import com.example.bet_api.model.Bulletin;
import com.example.bet_api.model.User;
import com.example.bet_api.repository.BetslipRepository;
import com.example.bet_api.service.BetslipService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BetslipServiceImpl implements BetslipService {
    private final BetslipRepository betslipRepository;

    //isolation = Isolation.REPEATABLE_READ, ?
    @Transactional(timeout = 2000, propagation = Propagation.REQUIRES_NEW)
    @Override
    public BetslipResponse create(User user, BetslipCreateRequest request) {
        var bulletin = bulletinRepository.findById(request.eventId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Bulletin with id %s not found", request.eventId())));
        var betslip = BetslipCreateRequest.toEntity(request);
        betslip.setCreatedBy(user);
        betslip.setCustomer(user);
        betslip.setBulletin(bulletin);

        var requestedOdds = this.getOdds(bulletin, betslip.getBetType());
        var lockedBulletin = bulletinRepository.findByIdWithReadLock(bulletin.getEventId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Bulletin with id %s not found", request.eventId())));
        var realizedOdds = this.getOdds(lockedBulletin, betslip.getBetType());
        betslip.setOddsRealized(realizedOdds);

        return BetslipResponse.toResponse(betslipRepository.save(betslip), requestedOdds);
    }

    @Transactional(timeout = 5000)
    @Override
    public BetslipResponse createBetslipWithOptimisticLockHandling(User user, BetslipCreateRequest request) {
        var betslip = BetslipCreateRequest.toEntity(request);
        betslip.setCreatedBy(user);
        betslip.setCustomer(user);

        var bulletin = bulletinRepository.findByIdWithReadLock(request.eventId()).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Bulletin with id %s not found", request.eventId())));
        betslip.setBulletin(bulletin);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }

        var requestedOdds = this.getOdds(bulletin, betslip.getBetType());
        betslip.setOddsRealized(requestedOdds);
        betslip = this.save(betslip);

        return BetslipResponse.toResponse(betslip, requestedOdds);

    }

    @Retryable(retryFor = {
            ObjectOptimisticLockingFailureException.class}, maxAttempts = 3, backoff = @Backoff(delay = 300, multiplier = 2))
    @Transactional(timeout = 5000, propagation = Propagation.REQUIRES_NEW)
    public Betslip save(Betslip betslip) {
        var bulletin = bulletinRepository.findByIdWithReadLock(betslip.getBulletin().getEventId()).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Bulletin with id %s not found", betslip.getBulletin().getEventId())));
        betslip.setBulletin(bulletin);
        betslip.setOddsRealized(this.getOdds(bulletin, betslip.getBetType()));

        return betslipRepository.save(betslip);
    }

    protected BigDecimal getOdds(Bulletin bulletin, BetType betType) {
        return switch (betType) {
            case HOME -> bulletin.getOddsHomeWin();
            case DRAW -> bulletin.getOddsDraw();
            case AWAY -> bulletin.getOddsAwayWin();
        };
    }
}
