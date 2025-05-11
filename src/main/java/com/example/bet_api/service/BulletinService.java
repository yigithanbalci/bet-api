package com.example.bet_api.service;

import com.example.bet_api.dto.BulletinCreateRequest;
import com.example.bet_api.dto.BulletinResponse;
import com.example.bet_api.dto.BulletinUpdateRequest;
import com.example.bet_api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BulletinService {

    /**
     * Create a new bulletin.
     *
     * @param user    doing the create operation
     * @param request the bulletin to create
     * @return the saved bulletin
     */
    BulletinResponse create(User user, BulletinCreateRequest request);

    /**
     * Update an existing bulletin.
     *
     * @param request the updated bulletin object
     * @return the updated bulletin
     */
    BulletinResponse update(BulletinUpdateRequest request);

    /**
     * Get a bulletin by its event ID.
     *
     * @param eventId the external event ID
     * @return an Optional containing the bulletin, if found
     */
    BulletinResponse getByEventId(Long eventId);

    /**
     * Retrieve all bulletins.
     *
     * @return a list of bulletins
     */
    Page<BulletinResponse> getAll(Pageable pageable);
}
