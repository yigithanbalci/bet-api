package com.example.bet_api.web;

import com.example.bet_api.exception.AuthorizationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
public class DummyController {

    @GetMapping("/auth-denied")
    public void authDenied() {
        throw new AuthorizationDeniedException("Authorization denied");
    }

    @GetMapping("/auth")
    public void auth() {
        throw new AuthorizationException("Authorization error");
    }

    @GetMapping("/not-found")
    public void notFound() {
        throw new EntityNotFoundException("Entity not found");
    }

    @GetMapping("/unsupported-media")
    public void unsupportedMedia() throws HttpMediaTypeNotSupportedException {
        throw new HttpMediaTypeNotSupportedException("Unsupported media type");
    }

    @GetMapping("/unreadable")
    public void unreadable() {
        throw new HttpMessageNotReadableException("Malformed JSON");
    }

    @GetMapping("/generic")
    public void generic() {
        throw new RuntimeException("Generic error");
    }
}
