package com.pixel.synchronre.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> resourceNotFoundException(MethodArgumentNotValidException ex, WebRequest request) throws JsonProcessingException {
        Map<String, String> errors = new HashMap<>();
        BindingResult bindingResult = ex.getBindingResult();
        if (bindingResult != null && bindingResult.hasErrors())
        {
            if(bindingResult.getFieldError() != null)
            {
                bindingResult.getFieldErrors().forEach(err->
                {
                    errors.put(err.getField(), err.getDefaultMessage());
                });
            }
            if(bindingResult.hasGlobalErrors())
            {
                bindingResult.getGlobalErrors().forEach(err->
                {
                    if(err.getDefaultMessage().contains("::"))
                        errors.put(err.getDefaultMessage().split("::")[0], err.getDefaultMessage().split("::")[1]);
                    else
                        errors.put("globalError", err.getDefaultMessage());
                });
            }
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("errors", new ObjectMapper().writeValueAsString(errors));

        return new ResponseEntity(errors, responseHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    Map<String, String> onConstraintValidationException(
            ConstraintViolationException e) {
        Map<String, String> error = new HashMap<>();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            error.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return error;
    }

    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String catchAppException(AppException e)
    {
        return e.getMessage();
    }
}
