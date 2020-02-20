package com.java.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    private final Logger LOG = LoggerFactory.getLogger(getClass());


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationException(ConstraintViolationException ex) {

        List<String> validationErrors = ex
                .getConstraintViolations()
                .stream()
                .map(x -> "The object " + x.getPropertyPath().toString().split("\\.")[1] + " has an invalid value: " + x.getInvalidValue() + " message: " + x.getMessage())
                .collect(Collectors.toList());


        Map<String, Object> response = new HashMap<>();
        response.put("message", validationErrors);
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                   WebRequest request) {


        Map<String, Object> response = new HashMap<>();

        String error = "The path variable '" + ex.getName() + "' must be an " + ex.getRequiredType().getName() + " type";
        response.put("message", error);


        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {

        List<String> validationErrors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> "The field " +
                        x.getField() + " has an invlalid value: " +
                        x.getRejectedValue() + " message: " +
                        x.getDefaultMessage()
                )
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("message", validationErrors);
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        String error = "A field: " + ex.getParameterName() + " was expected";

        Map<String, Object> response = new HashMap<>();
        response.put("message", error);

        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        String error = "It is not possible to process the request for the method '" + ex.getHttpMethod() + "' with the URL '" + ex.getRequestURL() + "'";

        Map<String, Object> response = new HashMap<>();
        response.put("message", error);

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append("The method '");
        builder.append(ex.getMethod());
        builder.append("' its not supported for this request. ");

        if (ex.getSupportedHttpMethods().size() == 0) {
            builder.append("The correct method is ");
            ex.getSupportedHttpMethods().forEach(t -> builder.append("'" + t + "'"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", builder.toString());

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append("The content type '");
        builder.append(ex.getContentType());
        builder.append("' is not supported. ");

        if (ex.getSupportedMediaTypes().size() > 1) {
            builder.append("The valid content types are: '");
            IntStream.range(0, ex.getSupportedMediaTypes().size())
                    .forEach(i -> {
                        if (i == (ex.getSupportedMediaTypes().size() - 1)) {
                            builder.append(ex.getSupportedMediaTypes().get(i) + "'");
                        } else {
                            builder.append(ex.getSupportedMediaTypes().get(i) + "', '");
                        }
                    });
        } else {
            builder.append("The valid content type is: '");
            ex.getSupportedMediaTypes().forEach(t -> builder.append(t + "'"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", builder.toString());

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "The are problems with the petition: " + ex.getLocalizedMessage();
        Map<String, Object> response = new HashMap<>();
        response.put("message", error);

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
