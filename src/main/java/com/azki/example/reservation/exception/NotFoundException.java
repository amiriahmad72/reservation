package com.azki.example.reservation.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public <T> NotFoundException(Class<?> clazz, T id) {
        super(String.format("Entity %s with id %s not found", clazz.getSimpleName(),
                id == null ? "NULL" : id.toString()));
    }

}
