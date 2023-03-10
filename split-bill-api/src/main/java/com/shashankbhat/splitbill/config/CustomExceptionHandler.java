package com.shashankbhat.splitbill.config;

import com.shashankbhat.exception.KnownException;
import com.shashankbhat.util.ErrorResponse;
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
        }else if(ex instanceof KnownException){
            KnownException knownException = (KnownException) ex;
            errorResponse.setError(knownException.getErrorMessage());
            errorResponse.setStatus(HttpStatus.BAD_REQUEST);
            errorResponse.setCode(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }else {
            // Log
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}