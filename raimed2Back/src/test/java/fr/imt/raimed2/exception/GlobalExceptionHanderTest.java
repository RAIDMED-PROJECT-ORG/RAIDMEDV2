package fr.imt.raimed2.exception;

import fr.imt.raimed2.exception.dto.response.Raimed2ErrorDetailsDto;
import fr.imt.raimed2.user.exception.EmailAlreadyInUseException;
import fr.imt.raimed2.user.exception.UsernameAlreadyInUseException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHanderTest {

    private final GlobalExceptionHander globalExceptionHander = new GlobalExceptionHander();

    @Test
    void handleMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "field", "defaultMessage");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, String>> response = globalExceptionHander.handleMethodArgumentNotValidException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("defaultMessage", response.getBody().get("field"));
    }

    @Test
    void handleUsernameAlreadyInUseException() {
        UsernameAlreadyInUseException exception = new UsernameAlreadyInUseException("user");

        ResponseEntity<String> response = globalExceptionHander.handleUsernameAlreadyInUseException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username user is already in use", response.getBody());
    }

    @Test
    void handleEmailAlreadyInUseException() {
        EmailAlreadyInUseException exception = new EmailAlreadyInUseException("test@email.com");

        ResponseEntity<String> response = globalExceptionHander.handleEmailAlreadyInUseException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email test@email.com is already in use", response.getBody());
    }

    @Test
    void handleHttpMessageNotReadableException() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Message not readable");

        ResponseEntity<String> response = globalExceptionHander.handleHttpMessageNotReadableException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Message not readable", response.getBody());
    }

    @Test
    void handleBadCredentialsException() {
        BadCredentialsException exception = new BadCredentialsException("Bad credentials");

        ResponseEntity<String> response = globalExceptionHander.handleBadCredentialsException(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Bad credentials", response.getBody());
    }

    @Test
    void handleNoHandlerFoundException() {
        ResponseEntity<String> response = globalExceptionHander.handleNoHandlerFoundException();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleNoSuchElementException() {
        NoSuchElementException exception = new NoSuchElementException("No such element");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test");

        ResponseEntity<Raimed2ErrorDetailsDto> response = globalExceptionHander.handleNoSuchElementException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals("No such element", response.getBody().getDetails());
        assertEquals("/test", response.getBody().getPath());
    }

    @Test
    void handleHttpRequestMethodNotSupportedException() {
        HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException("POST");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test");

        ResponseEntity<Raimed2ErrorDetailsDto> response = globalExceptionHander.handleHttpRequestMethodNotSupportedException(exception, request);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals("Method not allowed", response.getBody().getMessage());
        assertEquals("The method POST is not allowed for this endpoint", response.getBody().getDetails());
        assertEquals("/test", response.getBody().getPath());
    }

    @Test
    void handleAccessDeniedException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test");

        ResponseEntity<Raimed2ErrorDetailsDto> response = globalExceptionHander.handleAccessDeniedException(request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access denied", response.getBody().getMessage());
        assertEquals("You don't have the permission to access this resource", response.getBody().getDetails());
        assertEquals("/test", response.getBody().getPath());
    }

    @Test
    void handleNonValidRequestBody() {
        NonValidRequestBody exception = new NonValidRequestBody("Invalid body");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test");

        ResponseEntity<Raimed2ErrorDetailsDto> response = globalExceptionHander.handleNonValidRequestBody(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid request body", response.getBody().getMessage());
        assertEquals("Invalid body", response.getBody().getDetails());
        assertEquals("/test", response.getBody().getPath());
    }
}