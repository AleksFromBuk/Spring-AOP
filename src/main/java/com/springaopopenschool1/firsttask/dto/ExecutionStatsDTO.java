package com.springaopopenschool1.firsttask.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
@JsonIgnoreProperties
public class ExecutionStatsDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private final double averageExecutionTime;
    private final long minExecutionTime;
    private final long maxExecutionTime;
    private final boolean isAsyncExecuted;
}