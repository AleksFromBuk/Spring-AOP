package com.springaopopenschool1.firsttask.service;

import org.aspectj.lang.ProceedingJoinPoint;

public interface ExecutionLogService {
    void logExecutionTime(ProceedingJoinPoint joinPoint, long executionTime, boolean isAsyncExecuted);
}
