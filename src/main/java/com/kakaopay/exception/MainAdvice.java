package com.kakaopay.exception;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.kakaopay.constant.Constant;
import com.kakaopay.response.ReturnMessage;

@ControllerAdvice
public class MainAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Object processValidationError(MethodArgumentNotValidException ex) {
		ReturnMessage msg = ReturnMessage.builder().code(Constant.RES.INVALID_REQUESTBODY.getCode())
				.message(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()).build();
		return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public Object processValidationError(MethodArgumentTypeMismatchException ex) {
		ReturnMessage msg = ReturnMessage.builder().code(Constant.RES.INVALID_REQUESTBODY.getCode())
				.message(Constant.RES.INVALID_REQUESTBODY.getMessage()).build();
		return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public Object processValidationError(EntityNotFoundException ex) {
		ReturnMessage msg = ReturnMessage.builder().build();
		HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		if (ex.getMessage().equals(Constant.RES.INVALID_COUPON.getMessage())) {
			statusCode = HttpStatus.BAD_REQUEST;
			if (ex.getMessage().equals(Constant.RES.INVALID_COUPON.getMessage())) {
				msg = ReturnMessage.builder().code(Constant.RES.INVALID_COUPON.getCode())
						.message(Constant.RES.INVALID_COUPON.getMessage()).build();
			} else {
				msg = ReturnMessage.builder().code(Constant.RES.FAIL.getCode()).message(Constant.RES.FAIL.getMessage())
						.build();
			}

		} else {
			statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
			if (ex.getMessage().equals(Constant.RES.NO_COUPON.getMessage())) {
				msg = ReturnMessage.builder().code(Constant.RES.NO_COUPON.getCode())
						.message(Constant.RES.NO_COUPON.getMessage()).build();
			} else {
				msg = ReturnMessage.builder().code(Constant.RES.FAIL.getCode()).message(Constant.RES.FAIL.getMessage())
						.build();
			}

		}
		return new ResponseEntity<>(msg, statusCode);
	}

	@ExceptionHandler(ExpiredCouponException.class)
	public Object processValidationError(ExpiredCouponException ex) {
		ReturnMessage msg = ReturnMessage.builder().code(Constant.RES.EXPIRED_COUPON.getCode())
				.message(Constant.RES.EXPIRED_COUPON.getMessage()).build();
		return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(InvalidTokenException.class)
	public Object processValidationError(InvalidTokenException ex) {
		ReturnMessage msg = ReturnMessage.builder().code(Constant.RES.INVALID_TOKEN.getCode())
				.message(Constant.RES.INVALID_TOKEN.getMessage()).build();
		return new ResponseEntity<>(msg, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Object processValidationError(HttpMessageNotReadableException ex) {
		ReturnMessage msg = ReturnMessage.builder().code(Constant.RES.INVALID_REQUESTBODY.getCode())
				.message(Constant.RES.INVALID_REQUESTBODY.getMessage()).build();
		return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidUserException.class)
	public Object processValidationError(InvalidUserException ex) {
		ReturnMessage msg = ReturnMessage.builder().build();
		HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		if (ex.getMessage().equals(Constant.RES.USERID_ALREADY_USED.getMessage())) {
			statusCode = HttpStatus.BAD_REQUEST;
			msg = ReturnMessage.builder().code(Constant.RES.USERID_ALREADY_USED.getCode())
					.message(Constant.RES.USERID_ALREADY_USED.getMessage()).build();

		} else if (ex.getMessage().equals(Constant.RES.INCORRECT_PASSWORD.getMessage())) {
			statusCode = HttpStatus.UNAUTHORIZED;
			msg = ReturnMessage.builder().code(Constant.RES.INCORRECT_PASSWORD.getCode())
					.message(Constant.RES.INCORRECT_PASSWORD.getMessage()).build();

		} else if (ex.getMessage().equals(Constant.RES.USERID_NOT_EXIST.getMessage())) {
			statusCode = HttpStatus.UNAUTHORIZED;
			msg = ReturnMessage.builder().code(Constant.RES.USERID_NOT_EXIST.getCode())
					.message(Constant.RES.USERID_NOT_EXIST.getMessage()).build();

		} else if (ex.getMessage().equals(Constant.RES.USERID_NOT_MATCH.getMessage())) {
			statusCode = HttpStatus.UNAUTHORIZED;
			msg = ReturnMessage.builder().code(Constant.RES.USERID_NOT_MATCH.getCode())
					.message(Constant.RES.USERID_NOT_MATCH.getMessage()).build();

		} else {
			msg = ReturnMessage.builder().code(Constant.RES.FAIL.getCode()).message(Constant.RES.FAIL.getMessage())
					.build();
		}
		return new ResponseEntity<>(msg, statusCode);
	}
}