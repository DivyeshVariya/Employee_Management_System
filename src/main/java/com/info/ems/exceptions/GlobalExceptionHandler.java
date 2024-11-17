package com.info.ems.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import com.info.ems.constants.Constants;
import com.info.ems.dtos.response.Response;

@RestControllerAdvice
public class GlobalExceptionHandler {

	  @ExceptionHandler(EmployeeNotFoundException.class)
	  @ResponseStatus(HttpStatus.NOT_FOUND)
	  @ResponseBody
	  public ResponseEntity<Response> handleEmployeeNotFoundException(
			  EmployeeNotFoundException ex, HandlerMethod handlerMethod) {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND)
	        .body(
	            Response.builder()
	                .status(HttpStatus.NOT_FOUND)
	                .statusCode(HttpStatus.NOT_FOUND.value())
	                .message(ex.getMessage())
	                .data(
	                    Map.of(
	                        Constants.CLASS,
	                        handlerMethod.getBeanType().getSimpleName(),
	                        Constants.METHOD,
	                        handlerMethod.getMethod().getName()))
	                .build());
	  }
	  
	  @ExceptionHandler(EmployeeAlreadyExistsException.class)
	  @ResponseStatus(HttpStatus.CONFLICT)
	  @ResponseBody
	  public ResponseEntity<Response> handleEmployeeAlreadyExistsException(
			  EmployeeAlreadyExistsException ex, HandlerMethod handlerMethod) {
	    return ResponseEntity.status(HttpStatus.CONFLICT)
	        .body(
	            Response.builder()
	                .status(HttpStatus.CONFLICT)
	                .statusCode(HttpStatus.CONFLICT.value())
	                .message(ex.getMessage())
	                .data(
	                    Map.of(
	                        Constants.CLASS,
	                        handlerMethod.getBeanType().getSimpleName(),
	                        Constants.METHOD,
	                        handlerMethod.getMethod().getName()))
	                .build());
	  }
	  
	  @ExceptionHandler(Exception.class)
	  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	  @ResponseBody
	  public ResponseEntity<Response> handleException(
			  Exception ex, HandlerMethod handlerMethod) {
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	        .body(
	            Response.builder()
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
	                .message(ex.getMessage())
	                .data(
	                    Map.of(
	                        Constants.CLASS,
	                        handlerMethod.getBeanType().getSimpleName(),
	                        Constants.METHOD,
	                        handlerMethod.getMethod().getName()))
	                .build());
	  }
}
