package com.example.bet_api.web;

import com.example.bet_api.dto.BulletinCreateRequest;
import com.example.bet_api.dto.BulletinResponse;
import com.example.bet_api.dto.BulletinUpdateRequest;
import com.example.bet_api.model.User;
import com.example.bet_api.security.CurrentUser;
import com.example.bet_api.service.BulletinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping("/api/bulletins")
@RequiredArgsConstructor
@Tag(name = "Bulletin", description = "Bulletin Management APIs")
public class BulletinController {

    private final BulletinService bulletinService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a new event(bulletin)")
    public BulletinResponse create(@CurrentUser User user,
                                   @RequestBody @Valid @Parameter(description = "Bulletin create request")
                                   BulletinCreateRequest request) {
        return bulletinService.create(user, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    @Operation(summary = "Update a new event(bulletin)")
    public BulletinResponse update(@RequestBody @Valid @Parameter(description = "Bulletin update request")
                                   BulletinUpdateRequest request) {
        return bulletinService.update(request);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{eventId}")
    @Operation(summary = "Get an event(bulletin) by id")
    public BulletinResponse getByEventId(@PathVariable Long eventId) {
        return bulletinService.getByEventId(eventId);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    @Operation(summary = "Get all events(bulletin)")
    public Page<BulletinResponse> getAll(@PageableDefault(size = 10, sort = "startTimeUtc")
                                         @Parameter(description = "Pagination parameters") Pageable pageable) {
        return bulletinService.getAll(pageable);
    }

    @GetMapping(value = "/subscribe/{eventId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BulletinResponse> streamBulletins(@PathVariable Long eventId) {
        return Flux.interval(Duration.ofSeconds(1))
                .map(tick -> bulletinService.getByEventId(eventId));
    }
}
