package com.springaopopenschool1.firsttask.service;

import com.springaopopenschool1.firsttask.exception.ResourceNotFoundException;
import com.springaopopenschool1.firsttask.dto.ExecutionStatsDTO;
import com.springaopopenschool1.firsttask.model.ExecutionLog;
import com.springaopopenschool1.firsttask.repository.ExecutionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ExecutionStatsService {
    private static final String ERROR_MESSAGE = "data not found";
    private ExecutionLogRepository executionLogRepository;

    @Autowired
    public ExecutionStatsService(ExecutionLogRepository executionLogRepository) {
        this.executionLogRepository = executionLogRepository;
    }

    public ExecutionStatsDTO getExecutionStats() {
        Iterable<ExecutionLog> iterableLogs = executionLogRepository.findAll();

        List<ExecutionLog> logs = StreamSupport.stream(iterableLogs.spliterator(), false)
                .toList();

        if (logs.isEmpty()) {
            throw new ResourceNotFoundException(ERROR_MESSAGE);
        }

        Map<String, List<ExecutionLog>> groupedByMethod = logs.stream()
                .collect(Collectors.groupingBy(ExecutionLog::getMethodName));

        // среднее, минимальное и максимальное время выполнения

        double averageExecutionTime = groupedByMethod.values().stream()
                .flatMap(List::stream)
                .mapToDouble(ExecutionLog::getExecutionTime)
                .average()
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_MESSAGE));

        long minExecutionTime = groupedByMethod.values().stream()
                .flatMap(List::stream)
                .mapToLong(ExecutionLog::getExecutionTime)
                .min()
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_MESSAGE));

        long maxExecutionTime = groupedByMethod.values().stream()
                .flatMap(List::stream)
                .mapToLong(ExecutionLog::getExecutionTime)
                .max()
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_MESSAGE));

        return ExecutionStatsDTO.builder()
                .methodDetails(groupedByMethod)
                .averageExecutionTimeByAllMethods(averageExecutionTime)
                .minExecutionTime(minExecutionTime)
                .maxExecutionTime(maxExecutionTime)
                .build();
    }
}
