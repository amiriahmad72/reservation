package com.azki.example.reservation.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String VALIDATION_EXCEPTION = "VALIDATION_EXCEPTION";

    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiCallErrorWithDetail<String>> handleNotFoundException(HttpServletRequest request,
                                                                                  NotFoundException ex) {
        String code = "NOT_FOUND_EXCEPTION";
        log.error("{} {}\n", code, request.getRequestURI(), ex);
        String message = messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
        String detail = ex.getMessage() != null ? ex.getMessage() : "";
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiCallErrorWithDetail<>(code, message, detail));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiCallErrorWithDetail<String>> handleValidationException(HttpServletRequest request,
                                                                                    ValidationException ex) {
        String code = VALIDATION_EXCEPTION;
        log.error("{} {}\n", code, request.getRequestURI(), ex);
        String message = messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
        String detail = ex.getMessage() != null ? ex.getMessage() : "";
        return ResponseEntity
                .badRequest()
                .body(new ApiCallErrorWithDetail<>(code, message, detail));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiCallErrorWithDetail<String>> handleHttpMessageNotReadableException(
            HttpServletRequest request,
            HttpMessageNotReadableException ex) {
        String code = "HTTP_MESSAGE_NOT_READABLE_Exception";
        log.error("{} {}\n", code, request.getRequestURI(), ex);
        String message = messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
        String detail = ex.getMessage() != null ? ex.getMessage() : "";
        return ResponseEntity
                .badRequest()
                .body(new ApiCallErrorWithDetail<>(VALIDATION_EXCEPTION, message, detail));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiCallErrorWithDetail<String>> handleMissingServletRequestParameterException(
            HttpServletRequest request,
            MissingServletRequestParameterException ex) {
        String code = "MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION";
        log.error("{} {}\n", code, request.getRequestURI(), ex);
        String message = messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
        String detail = ex.getMessage() != null ? ex.getMessage() : "";
        return ResponseEntity
                .badRequest()
                .body(new ApiCallErrorWithDetail<>(VALIDATION_EXCEPTION, message, detail));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiCallErrorWithDetail<Map<String, String>>> handleMethodArgumentTypeMismatchException(
            HttpServletRequest request, MethodArgumentTypeMismatchException ex) {
        String code = "METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION";
        log.error("{} {}\n", code, request.getRequestURI(), ex);
        String message = messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
        Map<String, String> detail = new HashMap<>();
        detail.put("field", ex.getName());
        detail.put("errorMessage", ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new ApiCallErrorWithDetail<>(VALIDATION_EXCEPTION, message, detail));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiCallErrorWithDetail<List<Map<String, String>>>> handleMethodArgumentNotValidException(
            HttpServletRequest request, MethodArgumentNotValidException ex) {
        String code = "METHOD_ARGUMENT_NOT_VALID_EXCEPTION";
        log.error("{} {}\n", code, request.getRequestURI(), ex);
        String message = messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
        List<Map<String, String>> detail = new ArrayList<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> {
                    Map<String, String> fieldErrorMap = new HashMap<>();
                    fieldErrorMap.put("field", fieldError.getField());
                    fieldErrorMap.put("errorMessage", fieldError.getDefaultMessage());
                    detail.add(fieldErrorMap);
                });
        return ResponseEntity
                .badRequest()
                .body(new ApiCallErrorWithDetail<>(VALIDATION_EXCEPTION, message, detail));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiCallErrorWithDetail<String>> handleHttpRequestMethodNotSupportedException(
            HttpServletRequest request, HttpRequestMethodNotSupportedException ex) {
        String code = "HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION";
        log.error("{} {}\n", code, request.getRequestURI(), ex);
        String message = messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
        String detail = ex.getMessage() != null ? ex.getMessage() : "";
        return ResponseEntity
                .badRequest()
                .body(new ApiCallErrorWithDetail<>(code, message, detail));
    }

    @ExceptionHandler({Exception.class, InternalServerException.class})
    public ResponseEntity<ApiCallErrorWithDetail<String>> handleInternalServerError(HttpServletRequest request,
                                                                                    Exception ex) {
        String code = "INTERNAL_SERVER_EXCEPTION";
        log.error("{} {}\n", code, request.getRequestURI(), ex);
        String message = messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
        String detail = "";
        if (ex instanceof InternalServerException internalServerException) {
            detail = internalServerException.getMessage() != null ? internalServerException.getMessage() : "";
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiCallErrorWithDetail<>(code, message, detail));
    }

    @ExceptionHandler({NoResourceFoundException.class})
    public ResponseEntity<ApiCallErrorWithDetail<String>> handleNoResourceFoundException(HttpServletRequest request,
                                                                                         NoResourceFoundException ex) {
        String code = "NO_RESOURCE_FOUND_EXCEPTION";
        log.error("{} {}\n", code, request.getRequestURI(), ex);
        String message = messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
        String detail = ex.getMessage() != null ? ex.getMessage() : "";
        return ResponseEntity
                .badRequest()
                .body(new ApiCallErrorWithDetail<>(code, message, detail));
    }

    @ExceptionHandler({GenericException.class})
    private ResponseEntity<ApiCallError> handleGenericException(
            HttpServletRequest request, GenericException ex) {
        String code = ex.getMessage();
        log.error("{} {}", code, request.getRequestURI());
        log.debug("{} {}\n", code, request.getRequestURI(), ex);
        ApiCallError body;
        if (ex.getFieldName() == null && ex.getDetail() == null) {
            String message = getMessage(ex);
            body = new SimpleApiCallError(code, message);
        } else if (ex.getFieldName() != null) {
            String filedErrorMessage = messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
            List<Map<String, String>> detail = List.of(
                    Map.of(
                            "field", ex.getFieldName(),
                            "code", code,
                            "errorMessage", filedErrorMessage));
            String message = getMessage(ex);
            body = new ApiCallErrorWithDetail<>(VALIDATION_EXCEPTION, message, detail);
        } else {
            String message = getMessage(ex);
            body = new ApiCallErrorWithDetail<>(code, message, ex.getDetail());
        }
        return ResponseEntity.status(ex.getHttpStatus()).body(body);
    }

    private String getMessage(GenericException ex) {
        String code = ex.getMessage();
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public interface ApiCallError {
    }

    public record SimpleApiCallError(String code, String message) implements ApiCallError {
    }

    public record ApiCallErrorWithDetail<T>(String code, String message, T detail) implements ApiCallError {
    }

}
