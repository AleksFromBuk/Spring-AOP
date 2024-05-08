package com.springaopopenschool1.firsttask.repository;

import com.springaopopenschool1.firsttask.model.ExecutionLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("application-test")
public class ExecutionLogRepositoryTest {

    @Autowired
    private ExecutionLogRepository repository;

    @Test
    @Transactional
    public void testFindAverageExecutionTimeByMethodNameMultipleLogs() {
        ExecutionLog log1 = new ExecutionLog(UUID.randomUUID(), "TestClass",
                "specificMethod", 120, LocalDateTime.now(), false);
        ExecutionLog log2 = new ExecutionLog(UUID.randomUUID(), "TestClass",
                "specificMethod", 180, LocalDateTime.now(), false);
        ExecutionLog log3 = new ExecutionLog(UUID.randomUUID(), "TestClass",
                "specificMethod", 200, LocalDateTime.now(), false);
        repository.save(log1);
        repository.save(log2);
        repository.save(log3);

        Optional<Double> average = repository.findAverageExecutionTimeByMethodName("specificMethod");
        assertThat(average.get()).isEqualTo(166.66666666666666);
    }

    @Test
    @Transactional
    public void testFindMaxAndMinExecutionTimeByMethodName() {
        ExecutionLog log1 = new ExecutionLog(UUID.randomUUID(), "TestClass",
                "specificMethod", 120L, LocalDateTime.now(), false);
        ExecutionLog log2 = new ExecutionLog(UUID.randomUUID(), "TestClass",
                "specificMethod", 180L, LocalDateTime.now(), false);
        repository.save(log1);
        repository.save(log2);

        Optional<List<Object[]>> result = repository.findMaxAndMinExecutionTimeByMethodName("specificMethod");
        assertThat(result.get()).hasSize(1);
        assertThat(result.get().get(0)[0]).isEqualTo(180L);
        assertThat(result.get().get(0)[1]).isEqualTo(120L);
    }
}