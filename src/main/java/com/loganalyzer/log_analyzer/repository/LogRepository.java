package com.loganalyzer.log_analyzer.repository;

import com.loganalyzer.log_analyzer.model.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<LogEntry,Long> {
    List<LogEntry> findByType(String type);
}
