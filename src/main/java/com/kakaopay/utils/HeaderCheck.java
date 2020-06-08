package com.kakaopay.utils;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kakaopay.exeption.InvalidTokenException;

@Component
@Aspect
public class HeaderCheck {
	private static final Logger logger = LoggerFactory.getLogger(HeaderCheck.class);

	@Pointcut("execution(* com.kakaopay.controller.*.*(..))")
	public void tokenCheck() {
		logger.info("tokenCheck()실행");
	}

	@Before("execution(* com.kakaopay.controller.*.*(..))")
	public void beforeMethod() throws InvalidTokenException, UnsupportedEncodingException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String token = request.getHeader("Authorization");
		JWTUtils jwtUtils = new JWTUtils();
		if (!jwtUtils.validateToken(token))
			throw new InvalidTokenException();
		logger.info("beforeMethod()실행");
	}

}
