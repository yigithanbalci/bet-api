package com.example.bet_api.service;

import com.example.bet_api.dto.BetslipCreateRequest;
import com.example.bet_api.dto.BetslipResponse;
import com.example.bet_api.model.User;

public interface BetslipService {

    /**
     * Create a new bulletin.
     *
     * @param user    doing the create operation
     * @param request the betslip to create
     * @return the saved betslip
     */
    BetslipResponse create(User user, BetslipCreateRequest request);
}
