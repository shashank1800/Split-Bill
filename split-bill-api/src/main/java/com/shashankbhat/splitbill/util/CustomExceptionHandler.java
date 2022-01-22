package com.shashankbhat.splitbill.util;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> getStandardErrorMessage(Exception ex){

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDeveloperMessage(ex.getMessage());

        if(ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException mex = (MethodArgumentNotValidException) ex;
            List<String> errors = mex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            if(!errors.isEmpty()){
                errorResponse.setError(errors.get(0));
            }
        }


        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}