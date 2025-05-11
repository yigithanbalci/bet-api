package com.example.bet_api;

import com.example.bet_api.model.Bulletin;
import com.example.bet_api.model.User;
import com.example.bet_api.repository.BulletinRepository;
import com.example.bet_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BulletinRepository bulletinRepository;

    @BeforeEach
    void setUp() {
        bulletinRepository.deleteAll();
        userRepository.deleteAll();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String pass = bCryptPasswordEncoder.encode("1234");
        User user = User.builder().username("user").password(pass).role("USER").build();
        User admin = User.builder().username("admin").password(pass).role("ADMIN").build();
        User system = User.builder().username("system").password(pass).role("ADMIN").build();

        userRepository.save(user);
        User savedAdmin = userRepository.save(admin);
        userRepository.save(system);

        Bulletin bulletin = Bulletin.builder()
                .leagueName("Premier League")
                .homeTeam("Team A")
                .awayTeam("Team B")
                .oddsHomeWin(new BigDecimal("2.5"))
                .oddsDraw(new BigDecimal("3.2"))
                .oddsAwayWin(new BigDecimal("2.9"))
                .startTimeUtc(Instant.now())
                .build();
        bulletin.setCreatedBy(savedAdmin);
        bulletinRepository.save(bulletin);
    }

    @Test
    void shouldReturnUnauthorizedForAuthorizationDeniedException() {
        // Given: Simulate AuthorizationDeniedException
        String url = "/api/bulletins"; // Replace with the URL that would trigger AuthorizationDeniedException

        // When: Making a request that would trigger AuthorizationDeniedException
        var response = restTemplate
                .withBasicAuth("user", "wrongpassword") // Trigger authorization error
                .getForEntity(url, String.class);

        // Then: Validate the response status and body
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturnUnauthorizedForAuthorizationException() {
        // Given: Simulate AuthorizationException
        String url = "/api/bulletins"; // Replace with the URL that would trigger AuthorizationException

        // When: Making a request that would trigger AuthorizationException
        var response = restTemplate
                .withBasicAuth("user", "wrongpassword") // Trigger authorization error
                .getForEntity(url, String.class);

        // Then: Validate the response status and body
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturnBadRequestForMethodArgumentNotValidException() {
        // Given: Simulate invalid input that will cause MethodArgumentNotValidException
        String url = "/api/bulletins"; // Use an endpoint where MethodArgumentNotValidException may occur
        // Sample invalid request, change according to your request body
        String invalidRequestBody = "{ \"invalidField\": \"value\" }";

        // When: Making a request with invalid body
        var response = restTemplate
                .withBasicAuth("user", "1234")
                .postForEntity(url, invalidRequestBody, String.class);

        // Then: Validate the response status and body
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("invalidField"); // Assuming your error contains the field name
    }

    @Test
    void shouldReturnBadRequestForConstraintViolationException() {
        // Given: Simulate invalid data that causes a constraint violation
        String url = "/api/bulletins"; // Replace with the appropriate endpoint
        // Sample invalid request for constraint violation
        String invalidRequestBody = "{ \"someField\": \"invalidValue\" }";

        // When: Making a request that violates a constraint
        var response = restTemplate
                .withBasicAuth("user", "1234")
                .postForEntity(url, invalidRequestBody, String.class);

        // Then: Validate the response status and body
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("someField"); // Customize based on actual constraint violation message
    }

    @Test
    void shouldReturnInternalServerErrorForGeneralException() {
        // Given: Simulate a general exception (you could make a call that triggers a generic exception)
        String url = "/api/bulletins"; // Replace with a URL that will cause a generic error

        // When: Making a request that triggers an exception
        var response = restTemplate.withBasicAuth("user", "1234").getForEntity(url, String.class);

        // Then: Validate the response status and body
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).contains("Internal Server Error"); // Customize based on actual error message
    }
}
