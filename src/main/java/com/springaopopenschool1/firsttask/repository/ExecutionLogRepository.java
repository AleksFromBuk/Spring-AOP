package com.springaopopenschool1.firsttask.repository;

import com.springaopopenschool1.firsttask.model.ExecutionLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExecutionLogRepository extends CrudRepository<ExecutionLog, UUID> {
    @Query("SELECT AVG(el.executionTime) FROM ExecutionLog el")
    long findAverageExecutionTime();

    @Query("SELECT MIN(el.executionTime) FROM ExecutionLog el")
    long findMinExecutionTime();

    @Query("SELECT MAX(el.executionTime) FROM ExecutionLog el")
    long findMaxExecutionTime();

    @Query("SELECT AVG(e.executionTime) FROM ExecutionLog e WHERE e.methodName = ?1")
    Double findAverageExecutionTimeByMethodName(String methodName);

    @Query("SELECT MAX(e.executionTime), MIN(e.executionTime) FROM ExecutionLog e WHERE e.methodName = ?1")
    List<Object[]> findMaxAndMinExecutionTimeByMethodName(String methodName);
}
