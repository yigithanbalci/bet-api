package com.example.bet_api.web;

import com.example.bet_api.dto.BulletinCreateRequest;
import com.example.bet_api.dto.BulletinResponse;
import com.example.bet_api.model.User;
import com.example.bet_api.security.CurrentUser;
import com.example.bet_api.service.BulletinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Slf4j
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

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Get all events(bulletin)")
    public Flux<List<BulletinResponse>> live() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(tick -> bulletinService.getAll())
                .doOnCancel(() -> log.info("BulletinController live canceled"))
                .doOnError(error -> log.error("BulletinController live error", error));
    }
}
