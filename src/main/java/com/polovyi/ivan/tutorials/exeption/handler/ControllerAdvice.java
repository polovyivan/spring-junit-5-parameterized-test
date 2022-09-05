package com.polovyi.ivan.tutorials.exeption.handler;

import com.polovyi.ivan.tutorials.dto.response.RestErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

}
