package com.springaopopenschool1.firsttask.service;

import com.springaopopenschool1.firsttask.model.ExecutionLog;
import com.springaopopenschool1.firsttask.repository.ExecutionLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class ExecutionLogServiceImpl implements ExecutionLogService {
    private final ExecutionLogRepository repository;

    public ExecutionLogServiceImpl(ExecutionLogRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void logExecutionTime(ProceedingJoinPoint joinPoint, long timeExecuted, boolean isAsync) {
        try {
            ExecutionLog log = ExecutionLog.builder()
                    .id(UUID.randomUUID())
                    .className(joinPoint.getSignature().getDeclaringTypeName())
                    .methodName(joinPoint.getSignature().getName())
                    .executionTime(timeExecuted)
                    .createdAt(LocalDateTime.now())
                    .isAsync(isAsync)
                    .build();
            repository.save(log);
        } catch (Exception e) {
            log.error("Error when trying to complete a transaction", e);
        }
    }
}
