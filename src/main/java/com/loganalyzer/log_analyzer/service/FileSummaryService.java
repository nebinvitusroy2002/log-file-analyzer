package com.loganalyzer.log_analyzer.service;

import com.loganalyzer.log_analyzer.model.LogEntry;
import com.loganalyzer.log_analyzer.repository.LogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileSummaryService {

    private final LogRepository logRepository;

    public FileSummaryService(LogRepository logRepository){
        this.logRepository = logRepository;
    }

    public List<LogEntry> getLogDetailsByType(String filterType){
        return logRepository.findByType(filterType);
    }
}
