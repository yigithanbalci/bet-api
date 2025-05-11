package com.example.bet_api.dev;

import com.example.bet_api.model.User;
import com.example.bet_api.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
// NOTE: yigithanbalci 10.05.2025: Initialize default user (an example user) for devenv
public class DevEnvInitializer {

    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        log.info("Initializing dev environment");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String pass = bCryptPasswordEncoder.encode("1234");
        User user = User.builder().username("user").password(pass).role("USER").build();
        User admin = User.builder().username("admin").password(pass).role("ADMIN").build();
        User system = User.builder().username("system").password(pass).role("ADMIN").build();

        userRepository.save(user);
        userRepository.save(admin);
        userRepository.save(system);
        log.info("Finished initializing dev environment");
    }
}
