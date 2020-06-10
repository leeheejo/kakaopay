package com.kakaopay.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kakaopay.exception.InvalidTokenException;
import com.kakaopay.response.ReturnDefault;
import com.kakaopay.response.ReturnMessage;

@Component
@Aspect
public class Logging {

	private static final Logger logger = LoggerFactory.getLogger(Logging.class);

	@AfterReturning(pointcut = "execution( * com.kakaopay.controller..*(..))", returning = "ret")
	public void afterReturning(JoinPoint joinPoint, ResponseEntity<Object> ret) {
		logger.info("Response Code: " + ret.getStatusCodeValue());
		if (ret.getBody().getClass().getName().contains("ReturnMessage")) {
			ReturnMessage obj = ReturnMessage.class.cast(ret.getBody());
			logger.info("Response Body: " + obj.toString());
		} else if (ret.getBody().getClass().getName().contains("ReturnDefault")) {
			ReturnDefault obj = ReturnDefault.class.cast(ret.getBody());
			logger.info("Response Body: " + obj.toString());
		}
	}

	@Pointcut("execution(* com.kakaopay.controller.*.*(..))")
	public void mainLogging() {

	}

	@Pointcut("execution(* com.kakaopay.service.impl.*.*(..))")
	public void subLogging() {

	}

	@Around("within(com.kakaopay.controller.*)")
	public Object doMainLogging(ProceedingJoinPoint pjp) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		try {
			return pjp.proceed(pjp.getArgs());
		} finally {
			logger.info("Request: {} {} < {} ", request.getMethod(), request.getRequestURI(), request.getRemoteHost());
		}
	}

	@Around("within(com.kakaopay.service.impl.*)")
	public Object doSubLogging(ProceedingJoinPoint pjp) throws Throwable {
		logger.info("Call Service: {}",
				pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
		return pjp.proceed(pjp.getArgs());
	}

}
