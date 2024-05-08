package com.springaopopenschool1.firsttask.aspect;

import com.springaopopenschool1.firsttask.service.ExecutionLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MethodExecutionTrackingAspect {
    private final ExecutionLogService executionLogServiceImpl;

    @Autowired
    public MethodExecutionTrackingAspect(ExecutionLogService executionLogServiceImpl) {
        this.executionLogServiceImpl = executionLogServiceImpl;
    }


    @Around("@annotation(com.springaopopenschool1.firsttask.annotation.TrackTime)")
    public Object trackTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;
        executionLogServiceImpl.logExecutionTime(joinPoint, executionTime, false);
        return result;
    }

    @Around("@annotation(com.springaopopenschool1.firsttask.annotation.TrackAsyncTime)")
    public Object trackAsyncTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;
        executionLogServiceImpl.logExecutionTime(joinPoint, executionTime, true);
        return result;
    }
}
