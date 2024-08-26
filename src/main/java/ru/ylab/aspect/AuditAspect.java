package ru.ylab.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import org.aspectj.lang.annotation.*;
import ru.ylab.audit.AuditService;

import java.util.Arrays;

@Aspect
public class AuditAspect {
    private final AuditService auditService;

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @Pointcut("execution(* ru.ylab.service.*.*(..))")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        System.out.println("Method execution: " + joinPoint.getSignature().getName());
        System.out.println("Arguments: " + Arrays.toString(joinPoint.getArgs()));
        auditService.logAction(getUserId(), "BEFORE_EXECUTION", joinPoint.getSignature().getName());
    }

    @After("serviceMethods()")
    public void logAfterMethod(JoinPoint joinPoint) {
        System.out.println("Method execution completed: " + joinPoint.getSignature().getName());
        auditService.logAction(getUserId(), "AFTER_EXECUTION", joinPoint.getSignature().getName());
    }

    @Around("serviceMethods()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } finally {
            long timeTaken = System.currentTimeMillis() - startTime;
            System.out.println("Method execution time for " + joinPoint.getSignature().getName() + ": " + timeTaken + " ms");
            auditService.logAction(getUserId(), "EXECUTION_TIME", joinPoint.getSignature().getName() + ": " + timeTaken + " ms");
        }
        return result;
    }

    private int getUserId() {
        return AuditService.loggedInUser.getId();
    }
}
