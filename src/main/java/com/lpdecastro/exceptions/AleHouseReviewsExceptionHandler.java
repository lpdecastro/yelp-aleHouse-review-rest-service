package com.lpdecastro.exceptions;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path.Node;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.lpdecastro.dtos.ErrorResponseDto;

import lombok.extern.slf4j.Slf4j;

/**
 * @author liandre.p.de.castro
 *
 * @since 2021/05/05
 */
@Slf4j
@RestControllerAdvice
public class AleHouseReviewsExceptionHandler
        extends ResponseEntityExceptionHandler {

    private static final String LOGGER_TEMPLATE = "{}::{}() - {}";

    private static final String KEY_EXCEPTION = "exception";

    @ExceptionHandler(AleHouseReviewsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleAleHouseReviewsException(AleHouseReviewsException ex) {
        LOGGER.error(LOGGER_TEMPLATE, getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), ex);
        
        return getResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleConstraintViolationException(ConstraintViolationException ex) {
        final ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());

        final Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (final ConstraintViolation<?> error : violations) {
            LOGGER.debug(error.getPropertyPath() + ": " + error.getMessage());

            String field = "";
            final Iterator<Node> iter = error.getPropertyPath().iterator();
            while (iter.hasNext()) {
                final Node n = iter.next();
                final ElementKind kind = n.getKind();
                if (kind.equals(ElementKind.PARAMETER) || kind.equals(ElementKind.PROPERTY)) {
                    field = n.getName();
                }
            }

            errorResponse.addDetail(field, error.getMessage());
        }

        return errorResponse;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        LOGGER.error(LOGGER_TEMPLATE, getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), ex);

        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(LOGGER_TEMPLATE, getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), ex);

        return getResponseEntity(status, ex.getLocalizedMessage(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(LOGGER_TEMPLATE, getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), ex);

        return getResponseEntity(status, ex.getLocalizedMessage(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(LOGGER_TEMPLATE, getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), ex);

        return getResponseEntity(status, ex.getLocalizedMessage(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(LOGGER_TEMPLATE, getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), ex);
        return getResponseEntity(status, ex.getLocalizedMessage(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(LOGGER_TEMPLATE, getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), ex);

        final ErrorResponseDto response = new ErrorResponseDto(
                status.value(), status.getReasonPhrase());
        setFieldErrors(ex.getBindingResult(), response);

        return new ResponseEntity<>(response, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(LOGGER_TEMPLATE, getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), ex);

        return getResponseEntity(status, ex.getLocalizedMessage(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(LOGGER_TEMPLATE, getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), ex);

        return getResponseEntity(status, ex.getLocalizedMessage(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(LOGGER_TEMPLATE, getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), ex);

        return getResponseEntity(status, ex.getLocalizedMessage(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(LOGGER_TEMPLATE, getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(), ex);

        return getResponseEntity(status, ex.getLocalizedMessage(), headers);
    }

    private final ResponseEntity<Object> getResponseEntity(HttpStatus status, String message) {
        return getResponseEntity(status, message, new HttpHeaders());
    }

    private final ResponseEntity<Object> getResponseEntity(HttpStatus status, String message, HttpHeaders headers) {
        final ErrorResponseDto response = new ErrorResponseDto(
                status.value(), status.getReasonPhrase());

        response.addDetail(KEY_EXCEPTION, message);
        return new ResponseEntity<>(response, headers, status);
    }

    private void setFieldErrors(BindingResult bindingResult, ErrorResponseDto response) {
        for (final FieldError fieldError : bindingResult.getFieldErrors()) {
            LOGGER.debug(fieldError + ": " + fieldError.getDefaultMessage());

            final String field = fieldError.getField();
            final String objectName = fieldError.getObjectName();
            String message = fieldError.getDefaultMessage();

            if (objectName.contains("_")) {
                message = objectName + " " + message;

            } else if (!message.substring(0, field.length()).equalsIgnoreCase(field)) {
                message = field + " " + message;

            }

            response.addDetail(field, message);

        }

    }
}
