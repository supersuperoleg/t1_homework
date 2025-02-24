package com.rakhimov.homework.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ExceptionLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionLoggingAspect.class);

    @SuppressWarnings("unused")
    @AfterThrowing(
            pointcut = "@annotation(com.rakhimov.homework.aspect.annotation.ExceptionLog)",
            throwing = "exception"
    )
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        logger.error("Во время выполнения метода {}.{} с аргументами получено исключение: {}. Error: {}",
                className, methodName, args, exception.getMessage());

        logger.debug("Stack trace для {}.{}: ", className, methodName, exception);

        String alertMessage = String.format("ALERT: Критическая ошибка в %s.%s: %s", className, methodName, exception.getMessage());
        System.err.println(alertMessage);
    }
}
