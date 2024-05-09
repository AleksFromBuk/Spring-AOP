package com.springaopopenschool1.firsttask.service;

import com.springaopopenschool1.firsttask.exception.ResourceNotFoundException;
import com.springaopopenschool1.firsttask.dto.ExecutionStatsDTO;
import com.springaopopenschool1.firsttask.repository.ExecutionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
       Double averageExecutionTime = executionLogRepository
                .findAverageExecutionTime().orElseThrow(() -> new ResourceNotFoundException(ERROR_MESSAGE));
        Long minExecutionTime = executionLogRepository.findMinExecutionTime()
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_MESSAGE));
        Long maxExecutionTime = executionLogRepository.findMaxExecutionTime()
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_MESSAGE));


        return ExecutionStatsDTO.builder()
                .averageExecutionTime(averageExecutionTime)
                .minExecutionTime(minExecutionTime)
                .maxExecutionTime(maxExecutionTime)
                .build();
    }
}
