package com.springaopopenschool1.firsttask.controller;

import com.springaopopenschool1.firsttask.dto.ExecutionStatsDTO;
import com.springaopopenschool1.firsttask.service.ExecutionStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ExecutionStatsController {
    private final ExecutionStatsService statsService;

    @GetMapping("/stats")
    public ResponseEntity<ExecutionStatsDTO> getStats() {
        log.info("Request received for execution stats");
        ExecutionStatsDTO stats = statsService.getExecutionStats();
        log.info("Execution stats retrieved successfully");
        return ResponseEntity.ok(stats);
    }
}