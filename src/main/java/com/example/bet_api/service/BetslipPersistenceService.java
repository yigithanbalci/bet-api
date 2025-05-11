package com.example.bet_api.service;

import com.example.bet_api.model.Betslip;
import com.example.bet_api.model.User;

interface BetslipPersistenceService {

    /**
     * Create a new betslip with lockRetry
     *
     * @param user    doing the create operation
     * @param betslip the betslip to create
     * @return the saved betslip
     */
    Betslip create(User user, Betslip betslip);
}
