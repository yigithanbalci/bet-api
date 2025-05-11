package com.example.bet_api.service.impl;

import com.example.bet_api.dto.BulletinCreateRequest;
import com.example.bet_api.dto.BulletinResponse;
import com.example.bet_api.model.User;
import com.example.bet_api.repository.BulletinRepository;
import com.example.bet_api.service.BulletinService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    @Override
    public List<BulletinResponse> getAll() {
        var bulletins = bulletinRepository.findAll();
        if (bulletins.isEmpty()) {
            throw new EntityNotFoundException("No Bulletins found");
        }
        return bulletins.stream().map(BulletinResponse::toResponse).toList();
    }
}
