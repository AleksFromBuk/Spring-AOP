package com.springaopopenschool1.firsttask.aspect.asynclogresult;

import com.springaopopenschool1.firsttask.model.ExecutionLog;
import com.springaopopenschool1.firsttask.repository.ExecutionLogRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class SaveAsyncExecutionResultTest {

    @Autowired
    private SaveAsyncExecutionResult saveAsyncExecutionResult;

    @MockBean
    private ExecutionLogRepository repository;

    @MockBean
    private ProceedingJoinPoint joinPoint;

    @MockBean
    private Signature signature;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Test
    void testLogExecutionTimeAsyncIntegration() throws Throwable {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringTypeName()).thenReturn("com.example.ClassName");
        when(signature.getName()).thenReturn("methodName");
        when(joinPoint.getArgs()).thenReturn(new Object[]{joinPoint, 1000L, true});

        saveAsyncExecutionResult.logExecutionTimeAsync(joinPoint);

        verify(repository, timeout(1000).times(1)).save(any(ExecutionLog.class));
    }

    @BeforeEach
    void setUp() {
        try (AutoCloseable mock = MockitoAnnotations.openMocks(this)) {
            executorService = Executors.newSingleThreadExecutor();
            saveAsyncExecutionResult = new SaveAsyncExecutionResult(repository, executorService);

            when(joinPoint.getSignature()).thenReturn(signature);
            when(signature.getDeclaringTypeName()).thenReturn("com.example.ClassName");
            when(signature.getName()).thenReturn("methodName");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testLogExecutionTimeAsync() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{joinPoint, 1000L, true});

        saveAsyncExecutionResult.logExecutionTimeAsync(joinPoint);

        verify(repository, timeout(1000).times(1)).save(any(ExecutionLog.class));
    }

    @Test
    void testLogExecutionTimeAsync2() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{joinPoint, 1000L, true});

        Future<?> future = (Future<?>) saveAsyncExecutionResult.logExecutionTimeAsync(joinPoint);

        // Проверяем, что задача завершена
        future.get(); // Дожидаемся завершения асинхронной операции

        // Проверяем, что метод save был вызван с нужными параметрами
        verify(repository, times(1)).save(any(ExecutionLog.class));

        // Проверяем, что метод proceed был вызван для joinPoint
        verify(joinPoint, times(1)).proceed();
    }
}