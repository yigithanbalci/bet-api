package com.example.bet_api.repository;

import com.example.bet_api.model.Betslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetslipRepository extends JpaRepository<Betslip, Integer> {
}
