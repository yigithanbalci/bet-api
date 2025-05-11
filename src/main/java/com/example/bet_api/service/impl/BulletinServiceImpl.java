package com.example.bet_api.service.impl;

import com.example.bet_api.dto.BulletinCreateRequest;
import com.example.bet_api.dto.BulletinResponse;
import com.example.bet_api.dto.BulletinUpdateRequest;
import com.example.bet_api.model.User;
import com.example.bet_api.repository.BulletinRepository;
import com.example.bet_api.service.BulletinService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BulletinServiceImpl implements BulletinService {
    private final BulletinRepository bulletinRepository;

    @Override
    public BulletinResponse create(User user, @Valid BulletinCreateRequest request) {
        var bulletin = BulletinCreateRequest.toEntity(request);
        bulletin.setCreatedBy(user);
        return BulletinResponse.toResponse(bulletinRepository.save(bulletin));
    }

    @Override
    public BulletinResponse update(@Valid BulletinUpdateRequest request) {
        var bulletin = bulletinRepository.findById(request.eventId()).orElseThrow(() -> new EntityNotFoundException(String.format("Bulletin not found by id %d", request.eventId())));
        bulletin.setOddsHomeWin(request.oddsHomeWin());
        bulletin.setOddsDraw(request.oddsDraw());
        bulletin.setOddsAwayWin(request.oddsAwayWin());
        return BulletinResponse.toResponse(bulletinRepository.save(bulletin));
    }

    @Transactional(readOnly = true)
    @Override
    public BulletinResponse getByEventId(@Positive Long eventId) {
        var bulletin = bulletinRepository.findById(eventId).orElse(null);
        if (bulletin == null) {
            //NOTE: enable preview did not work for some reason & I don't want to debug that
            throw new EntityNotFoundException(String.format("Bulletin not found by id %d", eventId));
        }
        return BulletinResponse.toResponse(bulletin);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BulletinResponse> getAll(Pageable pageable) {
        var bulletins = bulletinRepository.findAll(pageable);
        if (bulletins.isEmpty()) {
            throw new EntityNotFoundException("No Bulletins found");
        }
        return bulletins.map(BulletinResponse::toResponse);
    }
}
