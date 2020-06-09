package com.kakaopay.aop;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class Logging {

	private static final Logger logger = LoggerFactory.getLogger(Logging.class);

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
		logger.debug("Call Service: {}",
				pjp.getSignature().getDeclaringTypeName() + ". " + pjp.getSignature().getName());
		return pjp.proceed(pjp.getArgs());
	}

}
