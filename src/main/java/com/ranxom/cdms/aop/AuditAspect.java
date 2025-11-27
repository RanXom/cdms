package com.ranxom.cdms.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ranxom.cdms.model.AuditLog;
import com.ranxom.cdms.repository.AuditLogRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public AuditAspect(AuditLogRepository auditLogRepository, ObjectMapper objectMapper) {
        this.auditLogRepository = auditLogRepository;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(auditable)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        Object result = joinPoint.proceed();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication != null ? authentication.getName() : "system";

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String methodName = signature.getName();

            AuditLog auditLog = new AuditLog();
            auditLog.setEntityType(auditable.entityType());
            auditLog.setAction(methodName);
            auditLog.setChangedBy(username);

            // Extract entity ID from result if possible
            if (result != null) {
                String entityId = extractEntityId(result);
                auditLog.setEntityId(entityId);
                auditLog.setDiff(objectMapper.writeValueAsString(result));
            }

            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            // Log error but don't fail the operation
            e.printStackTrace();
        }

        return result;
    }

    private String extractEntityId(Object entity) {
        try {
            if (entity.getClass().getMethod("getCaseId") != null) {
                return String.valueOf(entity.getClass().getMethod("getCaseId").invoke(entity));
            }
        } catch (Exception ignored) {
        }
        return "unknown";
    }
}
