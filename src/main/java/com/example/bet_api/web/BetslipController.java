package com.example.bet_api.web;

import com.example.bet_api.dto.BetslipCreateRequest;
import com.example.bet_api.dto.BetslipResponse;
import com.example.bet_api.model.User;
import com.example.bet_api.security.CurrentUser;
import com.example.bet_api.service.BetslipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/betslips")
@RequiredArgsConstructor
@Tag(name = "Betslip", description = "Betslip Management APIs")
public class BetslipController {

    private final BetslipService betslipService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Create a new betslip")
    public BetslipResponse create(@CurrentUser User user,
                                  @RequestBody @Valid @Parameter(description = "Betslip create request")
                                  BetslipCreateRequest request) {
        return betslipService.create(user, request);
    }
}
