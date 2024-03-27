package ru.yandex.practicum.filmorate.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUserNotFound(final EntityNotFoundException e) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}