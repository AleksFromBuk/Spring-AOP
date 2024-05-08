//package com.springaopopenschool1.firsttask.controller;
//
//import com.springaopopenschool1.firsttask.dto.ExecutionStatsDTO;
//import com.springaopopenschool1.firsttask.service.ExecutionStatsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class ExecutionStatsController {
//    private ExecutionStatsService statsService;
//
//    public ExecutionStatsController (ExecutionStatsService statsService) {
//        this.statsService = statsService;
//    }
//
//    @GetMapping("/stats")
//    public ResponseEntity<ExecutionStatsDTO> getStats() {
//        ExecutionStatsDTO stats = statsService.getExecutionStats();
//        return ResponseEntity.ok(stats);
//    }
//}
package com.springaopopenschool1.firsttask.controller;

import com.springaopopenschool1.firsttask.dto.ExecutionStatsDTO;
import com.springaopopenschool1.firsttask.service.ExecutionStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ExecutionStatsController {
    private final ExecutionStatsService statsService;

    @GetMapping("/stats")
    public ResponseEntity<ExecutionStatsDTO> getStats() {
        log.info("Request received for execution stats");
        try {
            ExecutionStatsDTO stats = statsService.getExecutionStats();
            log.info("Execution stats retrieved successfully");
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error retrieving execution stats", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving execution stats", e);
        }
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    }
}