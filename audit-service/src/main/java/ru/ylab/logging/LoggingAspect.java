package ru.ylab.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* ru.ylab.service.implementation.OrderServiceImpl.getAllOrders())")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } finally {
            long timeTaken = System.currentTimeMillis() - startTime;
            logger.info("Method execution time for {}: {} ms", joinPoint.getSignature().getName(), timeTaken);
        }
        return result;
    }
}
