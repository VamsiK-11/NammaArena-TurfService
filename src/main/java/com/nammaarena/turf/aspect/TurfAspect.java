package com.nammaarena.turf.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TurfAspect {

    @Before("execution(* com.nammaarena.turf.service.TurfService.createTurf(..))")
    public void auditTurfCreation(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length >= 2) {
            log.info("TURF_AUDIT: Turf creation by owner ID: {}", args[1]);
        }
    }

    @Before("execution(* com.nammaarena.turf.service.TurfService.updateTurfStatus(..))")
    public void auditTurfStatusChange(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length >= 2) {
            log.info("TURF_AUDIT: Status change - Turf ID: {}, Status: {}", args[0], args[1]);
        }
    }

    @Around("execution(* com.nammaarena.turf.service.TurfService.*(..))")
    public Object measurePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            log.info("TURF_PERFORMANCE: {} completed in {} ms", joinPoint.getSignature().toShortString(), executionTime);
            return result;
        } catch (Throwable throwable) {
            log.error("TURF_ERROR: {} failed - {}", joinPoint.getSignature().toShortString(), throwable.getMessage());
            throw throwable;
        }
    }
}