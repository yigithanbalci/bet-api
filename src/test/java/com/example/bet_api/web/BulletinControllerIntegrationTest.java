package com.example.bet_api.web;

import com.example.bet_api.dto.BulletinCreateRequest;
import com.example.bet_api.dto.BulletinResponse;
import com.example.bet_api.model.User;
import com.example.bet_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BulletinControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String pass = bCryptPasswordEncoder.encode("1234");
        User user = User.builder().username("user").password(pass).role("USER").build();
        User admin = User.builder().username("admin").password(pass).role("ADMIN").build();
        User system = User.builder().username("system").password(pass).role("ADMIN").build();

        userRepository.save(user);
        userRepository.save(admin);
        userRepository.save(system);
    }


    @Test
    void shouldCreateBulletinSuccessfully() {
        Instant time = ZonedDateTime.now().plusDays(1).toInstant();
        BulletinCreateRequest request = new BulletinCreateRequest(
                "Premier League",
                "Team A",
                "Team B",
                new BigDecimal("2.5"),
                new BigDecimal("3.2"),
                new BigDecimal("2.9"),
                time
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "1234");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<BulletinCreateRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<BulletinResponse>
                response = restTemplate.postForEntity("/api/bulletins", entity, BulletinResponse.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().leagueName()).isEqualTo("Premier League");
        assertThat(response.getBody().homeTeam()).isEqualTo("Team A");
        assertThat(response.getBody().awayTeam()).isEqualTo("Team B");
        assertThat(response.getBody().oddsHomeWin().toString()).isEqualTo("2.5");
        assertThat(response.getBody().oddsDraw().toString()).isEqualTo("3.2");
        assertThat(response.getBody().oddsAwayWin().toString()).isEqualTo("2.9");
        assertThat(response.getBody().startTimeUtc()).isEqualTo(time);
    }

    @Test
    void shouldStreamBulletinListEverySecond() {
        WebClient client = WebClient.create("http://localhost:8080"); // adjust port if needed

        Flux<List> stream = client.get()
                .uri("/api/bulletins/live")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(List.class);

        List firstBatch = stream.blockFirst();
        assertThat(firstBatch).isNotNull();
    }
}