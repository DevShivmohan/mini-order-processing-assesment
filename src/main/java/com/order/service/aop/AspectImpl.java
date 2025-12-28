package com.order.service.aop;

import com.order.service.annotations.RequiresPreAuthorizePermission;
import com.order.service.exception.GenericException;
import com.order.service.util.RequestContext;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@AllArgsConstructor
public class AspectImpl {

    @Around("@annotation(requiresPreAuthorizePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequiresPreAuthorizePermission requiresPreAuthorizePermission) throws Throwable {
        if (RequestContext.getUserFromRequestContextHolder().getUserId() == null || RequestContext.getUserFromRequestContextHolder().getRole() == null) {
            throw new GenericException(HttpStatus.UNAUTHORIZED.value(), "Unauthorized access");
        }
        if (Arrays.stream(requiresPreAuthorizePermission.value()).noneMatch(role -> role.equals(RequestContext.getUserFromRequestContextHolder().getRole()))) {
            throw new GenericException(HttpStatus.UNAUTHORIZED.value(), "Unauthorized access");
        }
        return joinPoint.proceed();
    }
}
