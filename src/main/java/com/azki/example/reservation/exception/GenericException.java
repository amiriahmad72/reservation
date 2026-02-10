package com.azki.example.reservation.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
public abstract class GenericException extends RuntimeException {

    private final String fieldName;
    private final Serializable detail;
    private final HttpStatus httpStatus;

    protected GenericException(String message, HttpStatus httpStatus) {
        super(message);
        this.fieldName = null;
        this.detail = null;
        this.httpStatus = httpStatus;
    }

    protected GenericException(String fieldName, String message, HttpStatus httpStatus) {
        super(message);
        this.fieldName = fieldName;
        this.detail = null;
        this.httpStatus = httpStatus;
    }


    protected GenericException(String message, Serializable detail, HttpStatus httpStatus) {
        super(message);
        this.fieldName = null;
        this.detail = detail;
        this.httpStatus = httpStatus;
    }

    protected GenericException(String fieldName, String message, Serializable detail, HttpStatus httpStatus) {
        super(message);
        this.fieldName = fieldName;
        this.detail = detail;
        this.httpStatus = httpStatus;
    }

    protected GenericException(String fieldName, String message, Throwable cause, Serializable detail, HttpStatus httpStatus) {
        super(message, cause);
        this.fieldName = fieldName;
        this.detail = detail;
        this.httpStatus = httpStatus;
    }

    protected GenericException(String fieldName, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
                               Serializable detail, HttpStatus httpStatus) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.fieldName = fieldName;
        this.detail = detail;
        this.httpStatus = httpStatus;
    }

}
