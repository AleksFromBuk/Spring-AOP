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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("application-test")
public class SaveAsyncExecutionResultTest {

    @Autowired
    private SaveAsyncExecutionResult saveAsyncExecutionResult;

    @Autowired
    private ExecutionLogRepository repository;

    @MockBean
    private ProceedingJoinPoint joinPoint;

    @MockBean
    private Signature signature;

    @BeforeEach
    void setUp() {
        try (AutoCloseable mock = MockitoAnnotations.openMocks(this)) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            saveAsyncExecutionResult = new SaveAsyncExecutionResult(repository, executorService);

            when(joinPoint.getSignature()).thenReturn(signature);
            when(signature.getDeclaringTypeName()).thenReturn("com.example.ClassName");
            when(signature.getName()).thenReturn("methodName");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAsyncLogExecution() throws Throwable {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringTypeName()).thenReturn("com.example.ClassName");
        when(signature.getName()).thenReturn("methodName");
        when(joinPoint.getArgs()).thenReturn(new Object[]{joinPoint, 1000L, true});

        Object[] args = new Object[]{joinPoint, 100L, true};
        when(joinPoint.getArgs()).thenReturn(args);

        // Вызов аспекта
        CompletableFuture<ExecutionLog> future = saveAsyncExecutionResult.logExecutionTimeAsync(joinPoint);

        // Проверка результатов
        ExecutionLog result = future.get();
        assertThat(result).isNotNull();
        assertThat(result.getExecutionTime()).isEqualTo(100L);
        assertThat(result.isAsyncExecuted()).isTrue();

        // Проверка, что данные сохранены в базу данных
        ExecutionLog savedLog = repository.findById(result.getId()).orElse(null);
        assertThat(savedLog).isNotNull();
        assert savedLog != null;
        assertThat(savedLog.getExecutionTime()).isEqualTo(100L);
        assertThat(savedLog.isAsyncExecuted()).isTrue();
    }
}
