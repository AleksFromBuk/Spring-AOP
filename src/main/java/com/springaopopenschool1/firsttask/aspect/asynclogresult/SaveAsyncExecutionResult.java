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
    private ExecutionLog executionLog;

    @Autowired
    public SaveAsyncExecutionResult(ExecutionLogRepository repository, @Qualifier("customExecutor") Executor executor) {
        this.repository = repository;
        this.customExecutor = executor;
    }

    @Pointcut("(execution (* com.springaopopenschool1.firsttask.service.ExecutionLogService.logExecutionTime(..)))")
    public void AsyncSaveResult() {
    }

    @Around("AsyncSaveResult()")
    public CompletableFuture<ExecutionLog> logExecutionTimeAsync(ProceedingJoinPoint joinPoint) {
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
                .isAsyncExecuted(methodType)
                .build();

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                String threadName = Thread.currentThread().getName();
                log.info("Asynchronously recording progress in a thread: {}", threadName);
             executionLog = saveWithRetry(currentResult);
            } catch (Exception e) {
                log.error("Error when trying to save asynchronously", e);
            }
        }, customExecutor);

        return future.handle((v, throwable) -> {
            if (throwable != null) {
                log.error("Error in async execution for method: {}", joinPointArg.getSignature().getName(), throwable);
            }
            return executionLog;
        });
    }

    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    private ExecutionLog saveWithRetry(ExecutionLog currentResult) {
        return repository.save(currentResult);
    }
}