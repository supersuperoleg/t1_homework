package com.rakhimov.homework.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;

@Component
@Aspect
public class RestMethodCallLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(RestMethodCallLoggingAspect.class);

    @SuppressWarnings("unused")
    @Before("@annotation(com.rakhimov.homework.aspect.annotation.RestMethodCallLog)")
    public void logRestMethodCallResult(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        String httpMethod = getHttpMethod(signature);

        logger.info("Вызван REST метод: {}.{} [{}] с аргументами: {}", className, methodName, httpMethod, args);
    }

    private String getHttpMethod(MethodSignature signature) {
        Annotation[] annotations = signature.getMethod().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof GetMapping) {
                return "GET";
            } else if (annotation instanceof PostMapping) {
                return "POST";
            } else if (annotation instanceof PutMapping) {
                return "PUT";
            } else if (annotation instanceof DeleteMapping) {
                return "DELETE";
            } else if (annotation instanceof RequestMapping) {
                RequestMapping requestMapping = (RequestMapping) annotation;
                if (requestMapping.method().length > 0) {
                    return requestMapping.method()[0].name();
                }
            }
        }
        return "UNKNOWN";
    }

}
