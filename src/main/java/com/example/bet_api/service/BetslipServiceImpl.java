package com.example.bet_api.service;

import com.example.bet_api.dto.BetslipCreateRequest;
import com.example.bet_api.dto.BetslipResponse;
import com.example.bet_api.model.User;
import com.example.bet_api.repository.BulletinRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BetslipServiceImpl implements BetslipService {
    private final BulletinRepository bulletinRepository;
    private final BetslipPersistenceService betslipPersistenceService;

    @Transactional(timeout = 5000)
    public BetslipResponse create(User user, BetslipCreateRequest request) {
        var bulletin = bulletinRepository.findById(request.eventId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Bulletin with id %s not found", request.eventId())));
        var requestedOdds = bulletin.getOdds(request.betType());
        var betslip = BetslipCreateRequest.toEntity(request);
        betslip.setBulletin(bulletin);
        betslip.setCreatedBy(user);
        betslip.setCustomer(user);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        return BetslipResponse.toResponse(betslipPersistenceService.create(user, betslip), requestedOdds);
    }
}
