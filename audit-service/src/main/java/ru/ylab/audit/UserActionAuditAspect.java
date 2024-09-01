package ru.ylab.audit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserActionAuditAspect {

    private final AuditService auditService;

    public UserActionAuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @AfterReturning("execution(* ru.ylab..*(..))")
    public void auditMethod(JoinPoint joinPoint) {

        String methodName = joinPoint.getSignature().getName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String details = "Method " + signature.getName() + " executed successfully.";

        auditService.logAction(methodName, details);
    }
}
