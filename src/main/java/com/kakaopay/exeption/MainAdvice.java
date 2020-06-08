package com.kakaopay.exeption;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.kakaopay.response.ReturnMessage;

@ControllerAdvice
public class MainAdvice {

	private static final Logger logger = LoggerFactory.getLogger(MainAdvice.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Object processValidationError(MethodArgumentNotValidException ex) {
		logger.info(ex.getMessage());
		ReturnMessage msg = ReturnMessage.builder()
				.message(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()).build();
		return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public Object processValidationError(MethodArgumentTypeMismatchException ex) {
		logger.info(ex.getMessage());
		ReturnMessage msg = ReturnMessage.builder().message("Invalid RequestBody").build();
		return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public Object processValidationError(EntityNotFoundException ex) {
		logger.info(ex.toString());
		ReturnMessage msg = ReturnMessage.builder().message(ex.getMessage()).build();
		HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		if (ex.getMessage().equals("Invalid Coupon")) {
			statusCode = HttpStatus.BAD_REQUEST;
		} else {
			statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(msg, statusCode);
	}

	@ExceptionHandler(ExpiredCouponException.class)
	public Object processValidationError(ExpiredCouponException ex) {
		logger.info(ex.getMessage());
		ReturnMessage msg = ReturnMessage.builder().message("Expired Coupon").build();
		return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(InvalidTokenException.class)
	public Object processValidationError(InvalidTokenException ex) {
		logger.info(ex.getMessage());
		ReturnMessage msg = ReturnMessage.builder().message("Invalid Token").build();
		return new ResponseEntity<>(msg, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Object processValidationError(HttpMessageNotReadableException ex) {
		logger.info(ex.getMessage());
		ReturnMessage msg = ReturnMessage.builder().message("Required request body is missing").build();
		return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidUserException.class)
	public Object processValidationError(InvalidUserException ex) {
		ReturnMessage msg = ReturnMessage.builder().message(ex.getMessage()).build();
		HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		if (ex.getMessage().equals("UserId is not exist")) {

			statusCode = HttpStatus.BAD_REQUEST;
		} else {
			statusCode = HttpStatus.UNAUTHORIZED;
		}

		return new ResponseEntity<>(msg, statusCode);
	}
}