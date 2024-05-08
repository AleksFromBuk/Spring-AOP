package com.springaopopenschool1.firsttask.controller;

import com.springaopopenschool1.firsttask.dto.ExecutionStatsDTO;
import com.springaopopenschool1.firsttask.repository.ExecutionLogRepository;
import com.springaopopenschool1.firsttask.service.ExecutionStatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ExecutionStatsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExecutionStatsService statsService;

    @InjectMocks
    private ExecutionStatsController statsController;

    @BeforeEach
    public void setup() {
        try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {
            mockMvc = MockMvcBuilders.standaloneSetup(statsController)
                    .setControllerAdvice(new ExceptionHandlerController())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getStats_ShouldReturnStats() throws Exception {
        ExecutionStatsDTO stats = ExecutionStatsDTO.builder()
                .maxExecutionTime(150)
                .averageExecutionTime(50)
                .minExecutionTime(100)
                .build();
        given(statsService.getExecutionStats()).willReturn(stats);

        mockMvc.perform(get("/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageExecutionTime").value(50))
                .andExpect(jsonPath("$.minExecutionTime").value(100))
                .andExpect(jsonPath("$.maxExecutionTime").value(150));
    }

    @Test
    public void getStats_ShouldHandleException() throws Exception {
        given(statsService.getExecutionStats()).willThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error retrieving execution stats"));
    }

    @Test
    public void test_controller_receives_get_request() {
        // Arrange
        ExecutionStatsService executionStatsService = mock(ExecutionStatsService.class);
        ExecutionStatsDTO expectedStats = ExecutionStatsDTO.builder()
                .averageExecutionTime(0)
                .minExecutionTime(0)
                .maxExecutionTime(0)
                .build();
        when(executionStatsService.getExecutionStats()).thenReturn(expectedStats);
        ExecutionStatsController controller = new ExecutionStatsController(executionStatsService);

        // Act
        ResponseEntity<ExecutionStatsDTO> response = controller.getStats();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStats, response.getBody());
    }

    @Test
    public void test_execution_log_repository_is_empty() {
        // Arrange
        ExecutionLogRepository executionLogRepository = mock(ExecutionLogRepository.class);
        when(executionLogRepository.findAverageExecutionTime()).thenReturn(0L);
        when(executionLogRepository.findMinExecutionTime()).thenReturn(0L);
        when(executionLogRepository.findMaxExecutionTime()).thenReturn(0L);
        ExecutionStatsService executionStatsService = new ExecutionStatsService(executionLogRepository);
        ExecutionStatsController controller = new ExecutionStatsController(executionStatsService);

        // Act
        ResponseEntity<ExecutionStatsDTO> response = controller.getStats();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ExecutionStatsDTO expectedStats = ExecutionStatsDTO.builder()
                .averageExecutionTime(0)
                .minExecutionTime(0)
                .maxExecutionTime(0)
                .build();
        assertEquals(expectedStats, response.getBody());
    }
}