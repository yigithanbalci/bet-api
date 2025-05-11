package com.example.bet_api.service;

import com.example.bet_api.model.Betslip;
import com.example.bet_api.model.User;
import com.example.bet_api.repository.BetslipRepository;
import com.example.bet_api.repository.BulletinRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BetslipPersistenceServiceImpl implements BetslipPersistenceService {
    private final BetslipRepository betslipRepository;
    private final BulletinRepository bulletinRepository;

    @Retryable(retryFor = {
            ObjectOptimisticLockingFailureException.class}, maxAttempts = 3, backoff = @Backoff(delay = 300, multiplier = 2))
    @Transactional(timeout = 2000, propagation = Propagation.REQUIRES_NEW)
    @Override
    public Betslip create(User user, Betslip betslip) {
        var bulletin = bulletinRepository.findByIdWithReadLock(betslip.getBulletin().getEventId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Bulletin with id [%s] not found", betslip.getId())));
        betslip.setBulletin(bulletin);
        betslip.setOddsRealized(bulletin.getOdds(betslip.getBetType()));
        return betslipRepository.save(betslip);
    }
}
