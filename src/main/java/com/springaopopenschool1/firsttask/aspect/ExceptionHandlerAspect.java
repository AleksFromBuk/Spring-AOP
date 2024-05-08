package com.springaopopenschool1.firsttask.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
@Order(1)
public class ExceptionHandlerAspect {
    @AfterThrowing(pointcut = "within(com.springaopopenschool1.firsttask.service.*) &&"
            + "execution(* * (..)) throws @com.springaopopenschool1.firsttask.annotation.Throw *)", throwing = "e"  )
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        log.info("Произошла ошибка при вызове метода: {}", joinPoint.getSignature().toShortString());
        log.info("ошибка: {}", e.getMessage());
    }
}
