package ru.yandex.practicum.filmorate.error;

public class ErrorResponse {
    String message;

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}