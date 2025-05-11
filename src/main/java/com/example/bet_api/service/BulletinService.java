package com.example.bet_api.service;

import com.example.bet_api.dto.BulletinCreateRequest;
import com.example.bet_api.dto.BulletinResponse;
import com.example.bet_api.model.User;

import java.util.List;

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
     * Retrieve all bulletins.
     *
     * @return a list of bulletins
     */
    List<BulletinResponse> getAll();
}
