package com.springaopopenschool1.firsttask.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Aspect
@Slf4j
public class AsyncRunnerAspect {

    @Pointcut("execution(@com.springaopopenschool1.firsttask.annotation.TrackAsyncTime public void add*(..))")
    public void asyncRunnerPointcut() {
    }

    @Around("asyncRunnerPointcut()")
    public CompletableFuture<Void> asyncRunner(ProceedingJoinPoint joinPoint) {
        return CompletableFuture.runAsync(() -> {
            try {
                log.info("Asynchronous execution in AsyncRunnerAspect");
                joinPoint.proceed();
            } catch (Throwable e) {
                log.error("Error in AsyncRunnerAspect", e);
                // Добавьте здесь вашу логику обработки ошибок, например, повторение попытки
            }
        }).whenComplete((result, error) -> {
            if (error != null) {
                log.error("Completion error in AsyncRunnerAspect", error);
            } else {
                log.info("Completed successfully in AsyncRunnerAspect");
            }
        });
    }
}
