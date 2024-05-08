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
    private ExecutionLogRepository executionLogRepository;

    @Autowired
    public ExecutionStatsService(ExecutionLogRepository executionLogRepository) {
        this.executionLogRepository = executionLogRepository;
    }

    public ExecutionStatsDTO getExecutionStats() {
       Double averageExecutionTime = executionLogRepository
                .findAverageExecutionTime().orElseThrow(() -> new ResourceNotFoundException("data not found"));
        Long minExecutionTime = executionLogRepository.findMinExecutionTime()
                .orElseThrow(() -> new ResourceNotFoundException("data not found"));
        Long maxExecutionTime = executionLogRepository.findMaxExecutionTime()
                .orElseThrow(() -> new ResourceNotFoundException("data not found"));


        return ExecutionStatsDTO.builder()
                .averageExecutionTime(averageExecutionTime)
                .minExecutionTime(minExecutionTime)
                .maxExecutionTime(maxExecutionTime)
                .build();
    }
}
