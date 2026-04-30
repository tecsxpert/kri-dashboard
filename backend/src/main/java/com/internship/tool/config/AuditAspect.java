package com.internship.tool.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * AOP Audit Aspect — logs entry, exit, execution time, and exceptions
 * for every method in the service implementation layer.
 */
@Aspect
@Component
@Slf4j
public class AuditAspect {

    @Around("execution(* com.internship.tool.service.impl.*.*(..))")
    public Object auditServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        log.info("[AUDIT] ▶ Entering  : {}", method);
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - start;
            log.info("[AUDIT] ✔ Completed : {} | elapsed={}ms", method, elapsed);
            return result;
        } catch (Exception ex) {
            long elapsed = System.currentTimeMillis() - start;
            log.error("[AUDIT] ✖ Exception : {} | elapsed={}ms | error={}", method, elapsed, ex.getMessage());
            throw ex;
        }
    }
}
