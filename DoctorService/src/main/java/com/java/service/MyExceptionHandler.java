package com.java.service;

import java.time.LocalDateTime;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.java.dto.Error;
import com.java.exception.DoctorRegisterException;

@RestControllerAdvice(basePackages = {"com.java.dao","com.java.controller"})
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ DataAccessException.class, DoctorRegisterException.class })
	public ResponseEntity<Error> handleError(Exception e, WebRequest req) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new com.java.dto.Error(500,
				"Error while executing " + req.getDescription(false) + e.getMessage(), LocalDateTime.now()));
	}

}
