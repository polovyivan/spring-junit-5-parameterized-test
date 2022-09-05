package com.polovyi.ivan.tutorials.exeption.handler;


import com.polovyi.ivan.dto.response.RestErrorResponse;
import com.polovyi.ivan.exeption.NotFoundException;
import com.polovyi.ivan.exeption.UnprocessableEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<RestErrorResponse>> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException e) {
        log.info("[ControllerAdvice] Processing MethodArgumentNotValidException...");
        List<RestErrorResponse> objectErrors = Optional.ofNullable(e)
                .map(MethodArgumentNotValidException::getBindingResult)
                .map(BindingResult::getAllErrors)
                .orElseGet(ArrayList::new)
                .stream()
                .map(ObjectError::getDefaultMessage)
                .map(RestErrorResponse::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(objectErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpStatus> notFoundExceptionHandler(NotFoundException e) {
        log.info("[ControllerAdvice] Processing NotFoundException...");
        return new ResponseEntity<>(e.getStatus());
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<RestErrorResponse> unprocessableEntityExceptionHandler(UnprocessableEntityException e) {
        log.info("[ControllerAdvice] Processing UnprocessableEntityException...");
        return new ResponseEntity<>(new RestErrorResponse(e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RestErrorResponse> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {
        log.info("[ControllerAdvice] Processing MethodArgumentTypeMismatchException...");
        String message = String.format("Field %s has an invalid format.", e.getName());
        return new ResponseEntity<>(new RestErrorResponse(message), HttpStatus.BAD_REQUEST);
    }
}
