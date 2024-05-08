package com.springaopopenschool1.firsttask.aspect.asynclogresult;

import com.springaopopenschool1.firsttask.model.ExecutionLog;
import com.springaopopenschool1.firsttask.repository.ExecutionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SaveAsyncExecutionResult {
    private Executor customExecutor;
    private ExecutionLogRepository repository;

    @Autowired
    public SaveAsyncExecutionResult(ExecutionLogRepository repository, @Qualifier("customExecutor") Executor executor) {
        this.repository = repository;
        this.customExecutor = executor;
    }

    @Pointcut("(execution (* com.springaopopenschool1.firsttask.service.ExecutionLogService.logExecutionTime(..)))")
    public void AsyncSaveResult() {
    }

    @Around("AsyncSaveResult()")
    public Object logExecutionTimeAsync(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        ProceedingJoinPoint joinPointArg = (ProceedingJoinPoint) args[0];
        long timeExecuted = (Long) args[1];
        boolean methodType = (Boolean) args[2];

        ExecutionLog currentResult = ExecutionLog.builder()
                .id(UUID.randomUUID())
                .className(joinPointArg.getSignature().getDeclaringTypeName())
                .methodName(joinPointArg.getSignature().getName())
                .executionTime(timeExecuted)
                .createdAt(LocalDateTime.now())
                .isAsync(methodType)
                .build();


        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                saveWithRetry(currentResult);
            } catch (Exception e) {
                log.error("Error when trying to save asynchronously", e);
            }
        }, customExecutor);

        return future.handle((v, throwable) -> {
            if (throwable != null) {
                log.error("Error when trying to save asynchronously", throwable);
            }
            try {
                return joinPoint.proceed();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    @Retryable(
            value = {Exception.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000)
    )
    private void saveWithRetry(ExecutionLog currentResult) {
        repository.save(currentResult);
    }
}