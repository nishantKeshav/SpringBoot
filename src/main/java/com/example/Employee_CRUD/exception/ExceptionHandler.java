package com.example.Employee_CRUD.exception;


import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.boot.json.JsonParseException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHandler {
    public void handleException(Exception e) throws UserException {
        if (e instanceof NullPointerException) {
            throw new UserException("Null value encountered", HttpStatus.BAD_REQUEST);
        } else if (e instanceof IllegalArgumentException) {
            throw new UserException("Invalid argument provided", HttpStatus.BAD_REQUEST);
        } else if (e instanceof JsonParseException || e instanceof JsonMappingException) {
            throw new UserException("Invalid JSON format", HttpStatus.BAD_REQUEST);
        } else if (e instanceof DataIntegrityViolationException) {
            throw new UserException("Database constraint violated", HttpStatus.BAD_REQUEST);
        } else if (e instanceof NumberFormatException) {
            throw new UserException("Invalid number format", HttpStatus.BAD_REQUEST);
        } else if (e instanceof UnsupportedOperationException) {
            throw new UserException("Unsupported operation", HttpStatus.METHOD_NOT_ALLOWED);
        } else {
            throw new UserException("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}