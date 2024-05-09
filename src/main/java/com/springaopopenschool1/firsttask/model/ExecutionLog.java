package com.springaopopenschool1.firsttask.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutionLog {
    @Id
    @UuidGenerator
    private UUID id;
    @NotNull
    private String className;
    @NotNull
    private String methodName;
    @NotNull
    private long executionTime;
    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private boolean isAsyncExecuted;
}
