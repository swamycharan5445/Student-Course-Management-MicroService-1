package org.example.main.exception;


import feign.FeignException;
import feign.RetryableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(InstructorNotFoundException.class)
    public ResponseEntity<String> handleException(InstructorNotFoundException ex)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(CourseNotFoundException.class)
    public  ResponseEntity<String> handleException(CourseNotFoundException ex)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> handleException(StudentNotFoundException ex)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignClientError(FeignException ex)
    {
        return ResponseEntity.status(ex.status()).body(ex.contentUTF8());
    }
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleException(WebClientResponseException ex)
    {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
    }
    @ExceptionHandler(HttpStatusCodeException.class)
    public  ResponseEntity<String> handleException(HttpStatusCodeException ex)
    {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
    }
    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<String> handleException(RestClientResponseException ex)
    {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
    }
    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<String> handleException(RetryableException ex)
    {
        return ResponseEntity.status(ex.status()).body(ex.getMessage());
    }


}
