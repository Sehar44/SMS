package com.example.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testUserAlreadyExistsException() {
        // Given
        UserAlreadyExistsException exception = new UserAlreadyExistsException("User already exists");

        // When
        ResponseEntity<String> response = globalExceptionHandler.userAlreadyExists(exception);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    void testInvalidAgeException() {
        // Given
        InvalidAgeException exception = new InvalidAgeException("Invalid age");

        // When
        ResponseEntity<String> response = globalExceptionHandler.invalidAge(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid age", response.getBody());
    }
    
    
    @Test
    void testUserNotFoundException() {
		// Given
		UserNotFoundException exception = new UserNotFoundException("Student not found");

		// When
		ResponseEntity<String> response = globalExceptionHandler.userNotFound(exception);

		// Then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Student not found", response.getBody());
	}
    
    @Test
    void testNoticeNotFound() {
    	NoticeNotFoundException exception = new NoticeNotFoundException("Notice not found");
		ResponseEntity<String> response = globalExceptionHandler.noticeNotFound(exception);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Notice not found", response.getBody());
    }
}
