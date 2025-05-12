package com.example.bet_api;

import com.example.bet_api.exception.GlobalExceptionHandler;
import com.example.bet_api.web.DummyController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private DummyController dummyController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(dummyController)     // instantiate controller.
                .setControllerAdvice(new GlobalExceptionHandler())   // bind with controller advice.
                .build();
    }

    @Test
    void shouldHandleAuthorizationDeniedException() throws Exception {
        mockMvc.perform(get("/auth-denied"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("\"Authorization denied\""));
    }

    @Test
    void shouldHandleAuthorizationException() throws Exception {
        mockMvc.perform(get("/auth"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("\"Authorization error\""));
    }

    @WithMockUser(username = "testUser", roles = {"USER"})
    @Test
    void shouldHandleEntityNotFoundException() throws Exception {
        mockMvc.perform(get("/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("\"Entity not found\""));
    }

//    @Test
//    void shouldHandleMismatchedInputException() throws Exception {
//        mockMvc.perform(get("/mismatch"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("\"Mismatch error\""));
//    }

    @WithMockUser(username = "testUser", roles = {"USER"})
    @Test
    void shouldHandleUnsupportedMediaType() throws Exception {
        mockMvc.perform(get("/unsupported-media").contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(content().string("\"Unsupported media type\""));
    }

    @Test
    void shouldHandleHttpMessageNotReadableException() throws Exception {
        mockMvc.perform(get("/unreadable"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Malformed JSON\""));
    }

    @Test
    void shouldHandleGenericException() throws Exception {
        mockMvc.perform(get("/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("\"Generic error\""));
    }

    @Configuration
    @EnableWebSecurity
    public static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(auth -> auth
                                                   .anyRequest().authenticated()
                                          );
            return http.build();
        }
    }
}
