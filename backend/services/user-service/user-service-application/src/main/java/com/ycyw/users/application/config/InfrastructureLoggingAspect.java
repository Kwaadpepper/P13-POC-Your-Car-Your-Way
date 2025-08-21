package com.ycyw.users.application.config;

import org.springframework.stereotype.Component;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Aspect qui place MDC key "service" = "user-service-infrastructure" pour tout code du package
 * com.ycyw.users.infrastructure..
 */
@Aspect
@Component
public class InfrastructureLoggingAspect {

  private static final Logger log = LoggerFactory.getLogger(InfrastructureLoggingAspect.class);
  private static final String MDC_KEY = "service";
  private static final String MDC_VALUE = "user-service-infrastructure";

  // Pointcut explicite ciblant le package infrastructure
  @Around("within(com.ycyw.users.infrastructure..*)")
  public Object wrapWithMdc(ProceedingJoinPoint pjp) throws Throwable {
    String previous = MDC.get(MDC_KEY);
    try {
      MDC.put(MDC_KEY, MDC_VALUE);
      if (log.isDebugEnabled()) {
        log.debug("MDC set [{}={}], entering: {}", MDC_KEY, MDC_VALUE, pjp.getSignature());
      }
      return pjp.proceed();
    } finally {
      if (log.isDebugEnabled()) {
        log.debug("Clearing MDC [{}], leaving: {}", MDC_KEY, pjp.getSignature());
      }
      if (previous == null) {
        MDC.remove(MDC_KEY);
      } else {
        MDC.put(MDC_KEY, previous);
      }
    }
  }
}
