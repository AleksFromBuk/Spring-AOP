package com.springaopopenschool1.firsttask.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springaopopenschool1.firsttask.model.ExecutionLog;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Builder
@Data
@JsonIgnoreProperties
public class ExecutionStatsDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Map<String, List<ExecutionLog>> methodDetails;
    private double averageExecutionTimeByAllMethods;
    private long minExecutionTime;
    private long maxExecutionTime;
}