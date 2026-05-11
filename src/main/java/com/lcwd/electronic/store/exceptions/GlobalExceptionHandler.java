package com.lcwd.electronic.store.exceptions;

import com.lcwd.electronic.store.dtos.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    //handler resource not found
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        logger.info("Exception Handler invoked !!");
        ApiResponse response = ApiResponse.builder().errors(List.of(ex.getMessage())).status(HttpStatus.NOT_FOUND).success(true).build();
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);

    }

    //MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        Map<String, Object> response = new HashMap<>();
        allErrors.stream().forEach(objectError -> {
            String message = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();
            response.put(field, message);
        });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }


    //handle bad api exception
    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponse> handleBadApiRequest(BadApiRequestException ex) {
        logger.info("Bad api request");
        ApiResponse response = ApiResponse.builder().errors(List.of(ex.getMessage())).status(HttpStatus.BAD_REQUEST).success(false).build();
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);

    }

}
