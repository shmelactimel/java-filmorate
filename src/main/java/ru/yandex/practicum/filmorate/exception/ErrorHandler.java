package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleUnknownFilmException(final FilmNotFoundException e) {
        return new ResponseEntity<>(
                Map.of("Ошибка", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleUnknownUserException(final UserNotFoundException e) {
        return new ResponseEntity<>(
                Map.of("Ошибка", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}
