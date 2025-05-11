package com.example.bet_api.service.impl;

import com.example.bet_api.dto.BetType;
import com.example.bet_api.dto.BetslipCreateRequest;
import com.example.bet_api.dto.BetslipResponse;
import com.example.bet_api.model.Bulletin;
import com.example.bet_api.model.User;
import com.example.bet_api.repository.BetslipRepository;
import com.example.bet_api.repository.BulletinRepository;
import com.example.bet_api.service.BetslipService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final BulletinRepository bulletinRepository;

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


    protected BigDecimal getOdds(Bulletin bulletin, BetType betType) {
        return switch (betType) {
            case HOME -> bulletin.getOddsHomeWin();
            case DRAW -> bulletin.getOddsDraw();
            case AWAY -> bulletin.getOddsAwayWin();
        };
    }
}
