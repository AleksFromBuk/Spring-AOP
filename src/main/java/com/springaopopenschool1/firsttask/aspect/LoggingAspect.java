package com.springaopopenschool1.firsttask.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("bean(plantService)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();
        log.info("выполнение метода {} с аргументами {}", methodName, methodArgs);
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Метод {} выполнился за {} мс с результатом {}", methodName, endTime - startTime, result);
        return result;
    }
}
