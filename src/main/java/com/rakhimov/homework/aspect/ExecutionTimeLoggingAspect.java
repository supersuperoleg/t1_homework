package com.rakhimov.homework.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ExecutionTimeLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionTimeLoggingAspect.class);

    @SuppressWarnings("unused")
    @Around("@annotation(com.rakhimov.homework.aspect.annotation.ExecutionTimeLog)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        long startTime = System.currentTimeMillis();

        // Выполняем метод
        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - startTime;
        logger.info("Метод {}.{} завершился за {} мс с результатом: {}", className, methodName, duration, result);

        return result;
    }
}
