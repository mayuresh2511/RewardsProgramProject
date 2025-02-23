package com.maymar.rewards.program.exception;

import com.maymar.rewards.program.exception.custom.InvalidUserIdException;
import com.maymar.rewards.program.exception.custom.NoTransactionsFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserIdException(InvalidUserIdException invalidUserIdException){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                "1000",
                invalidUserIdException.getMessage(),
                "Please enter userId in a valid format...");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<ErrorResponse> handleDateTimeException(DateTimeException dateTimeException){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                "1001",
                dateTimeException.getMessage(),
                "Please enter date in a valid format...");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException noSuchElementException){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                "1002",
                noSuchElementException.getMessage(),
                "It seems there are no transactions for you....");
        return new ResponseEntity<>(errorResponse, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(NoTransactionsFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoTransactionsFoundException(NoTransactionsFoundException noTransactionsFoundException){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                "1002",
                noTransactionsFoundException.getMessage(),
                "It seems there are no transactions for you....");
        return new ResponseEntity<>(errorResponse, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException nullPointerException){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                "5001",
                nullPointerException.getMessage(),
                "Something went too wrong. Please try again after sometime we are working on it...");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException runtimeException){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                "5002",
                runtimeException.getMessage(),
                "Something went too wrong. Please try again after sometime we are working on it...");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
