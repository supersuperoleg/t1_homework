package com.rakhimov.homework.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResultLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ResultLoggingAspect.class);

    @SuppressWarnings("unused")
    @AfterReturning(
            pointcut = "@annotation(com.rakhimov.homework.aspect.annotation.ResultLog)",
            returning = "result"
    )
    public void logMethodResult(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        logger.info("Метод {}.{} выполнился успешно с аргументами: {}, результат: {}",
                className, methodName, args, result);

        // Проверяем, что результат не null
        if (result == null) {
            logger.warn("Метод {}.{} вернул null, что не предусмотрено!", className, methodName);
        } else {
            if (result instanceof ResponseEntity<?> response) {
                logger.info("Статус ответа для {}.{}: {}", className, methodName, response.getStatusCode());
            }
        }
    }
}
