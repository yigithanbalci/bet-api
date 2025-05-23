package com.example.bet_api.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleAuthorizationException(AuthorizationDeniedException e) {
        log.error("Authorization denied error: {} ", e.getLocalizedMessage(), e);
        try {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(objectMapper.writeValueAsString(e.getLocalizedMessage()));
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Json Processing error: {} ", jsonProcessingException.getLocalizedMessage(),
                      jsonProcessingException);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleAuthorizationException(AuthorizationException e) {
        log.error("Authorization error: {} ", e.getLocalizedMessage(), e);
        try {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(objectMapper.writeValueAsString(e.getLocalizedMessage()));
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Json Processing error: {} ", jsonProcessingException.getLocalizedMessage(),
                      jsonProcessingException);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFoundException(EntityNotFoundException e) {
        log.error("Not found error: {} ", e.getLocalizedMessage(), e);
        try {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(objectMapper.writeValueAsString(e.getLocalizedMessage()));
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Json Processing error: {} ", jsonProcessingException.getLocalizedMessage(),
                      jsonProcessingException);
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(
            MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(objectError -> {
            String fieldName = ((FieldError) objectError).getField();
            String errorMessage = objectError.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Validity check exception: {} ", e.getLocalizedMessage(), e);
        try {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(objectMapper.writeValueAsString(errors));
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Json Processing error: {} ", jsonProcessingException.getLocalizedMessage(),
                      jsonProcessingException);
            return ResponseEntity.badRequest().build();
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(
            ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(constraintViolation -> {
            String propertyPath = constraintViolation.getPropertyPath().toString();
            String errorMessage = constraintViolation.getMessage();
            errors.put(propertyPath, errorMessage);
        });
        log.error("Validity check error: {} ", e.getLocalizedMessage(), e);
        try {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(objectMapper.writeValueAsString(errors));
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Json Processing error: {} ", jsonProcessingException.getLocalizedMessage(),
                      jsonProcessingException);
            return ResponseEntity.badRequest().build();
        }
    }

    @ExceptionHandler(MismatchedInputException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMismatchedException(MismatchedInputException e) {
        log.error("Mismatched error: {} ", e.getLocalizedMessage(), e);
        try {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(objectMapper.writeValueAsString(e.getLocalizedMessage()));
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Json Processing error: {} ", jsonProcessingException.getLocalizedMessage(),
                      jsonProcessingException);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseEntity<String> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        log.error("Unsupported Media Type: {}", e.getLocalizedMessage(), e);
        try {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body(objectMapper.writeValueAsString(e.getLocalizedMessage()));
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Json Processing error: {} ", jsonProcessingException.getLocalizedMessage(),
                      jsonProcessingException);
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.error("Malformed JSON or Unsupported Content: {}", e.getLocalizedMessage(), e);
        try {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(objectMapper.writeValueAsString(e.getLocalizedMessage()));
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Json Processing error: {} ", jsonProcessingException.getLocalizedMessage(),
                      jsonProcessingException);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Internal Server Error: {} ", e.getLocalizedMessage(), e);
        try {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(objectMapper.writeValueAsString(e.getLocalizedMessage()));
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Json Processing error: {} ", jsonProcessingException.getLocalizedMessage(),
                      jsonProcessingException);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
