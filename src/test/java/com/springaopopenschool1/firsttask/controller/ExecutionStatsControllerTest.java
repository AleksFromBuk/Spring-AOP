package com.springaopopenschool1.firsttask.controller;

import com.springaopopenschool1.firsttask.dto.ExecutionStatsDTO;
import com.springaopopenschool1.firsttask.exception.ExceptionHandlerGlobal;
import com.springaopopenschool1.firsttask.exception.ResourceNotFoundException;
import com.springaopopenschool1.firsttask.repository.ExecutionLogRepository;
import com.springaopopenschool1.firsttask.service.ExecutionStatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExecutionStatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExecutionStatsService statsService;

    @Autowired
    private ExecutionLogRepository executionLogRepository;

    @Autowired
    private ExecutionStatsController statsController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        MockMvcBuilders.standaloneSetup(statsController)
                    .setControllerAdvice(new ExceptionHandlerGlobal())
                    .build();
        executionLogRepository.deleteAll();
    }

    @Test
    public void getStatsShouldReturnStats() throws Exception {
        ExecutionStatsDTO stats = ExecutionStatsDTO.builder()
                .maxExecutionTime(150)
                .averageExecutionTimeByAllMethods(50)
                .minExecutionTime(100)
                .build();
        given(statsService.getExecutionStats()).willReturn(stats);

        mockMvc.perform(get("/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageExecutionTimeByAllMethods").value(50))
                .andExpect(jsonPath("$.minExecutionTime").value(100))
                .andExpect(jsonPath("$.maxExecutionTime").value(150));
    }

    @Test
    public void getStatsShouldHandleException() throws Exception {
        given(statsService.getExecutionStats()).willThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unexpected error"));
    }

    @Test
    public void testGetStatsWhenRepositoryIsEmpty() throws Exception {
        when(statsService.getExecutionStats()).thenThrow(new ResourceNotFoundException("data not found"));
        mockMvc.perform(get("/stats"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("data not found"));
    }
}