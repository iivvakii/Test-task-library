package org.example.testtasknerdy.exception;

import jakarta.validation.ConstraintViolationException;
import org.example.testtasknerdy.dto.ErrorDetailsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@RestControllerAdvice
public class WebExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetailsDto> handleConstraintViolationException(ConstraintViolationException exception, WebRequest request) {
        StringJoiner stringJoiner = new StringJoiner(", ");

        exception.getConstraintViolations().forEach(violation -> {
            stringJoiner.add(violation.getPropertyPath() + ": " + violation.getMessage());
        });

        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(
                LocalDateTime.now(),
                stringJoiner.toString(),
                request.getDescription(false),
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(errorDetailsDto, HttpStatus.BAD_REQUEST);
    }

}
