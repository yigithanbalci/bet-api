package com.example.bet_api.web;

import com.example.bet_api.dto.BulletinCreateRequest;
import com.example.bet_api.dto.BulletinResponse;
import com.example.bet_api.dto.BulletinUpdateRequest;
import com.example.bet_api.model.User;
import com.example.bet_api.security.CurrentUser;
import com.example.bet_api.service.BulletinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bulletins")
@RequiredArgsConstructor
public class BulletinController {

    private final BulletinService bulletinService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public BulletinResponse create(@CurrentUser User user, @RequestBody @Valid BulletinCreateRequest request) {
        return bulletinService.create(user, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public BulletinResponse update(@RequestBody @Valid BulletinUpdateRequest request) {
        return bulletinService.update(request);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{eventId}")
    public BulletinResponse getByEventId(@PathVariable Long eventId) {
        return bulletinService.getByEventId(eventId);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public Page<BulletinResponse> getAll(@PageableDefault(size = 10, sort = "startTimeUtc") Pageable pageable) {
        return bulletinService.getAll(pageable);
    }
}
