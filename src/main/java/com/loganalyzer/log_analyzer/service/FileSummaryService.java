package com.loganalyzer.log_analyzer.service;

import com.loganalyzer.log_analyzer.model.LogEntry;
import com.loganalyzer.log_analyzer.repository.LogRepository;
import com.loganalyzer.log_analyzer.service.interfaces.FileSummaryServiceInterface;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service

public class FileSummaryService implements FileSummaryServiceInterface {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FileSummaryService.class);


    private final LogRepository logRepository;

    public FileSummaryService(LogRepository logRepository){
        this.logRepository = logRepository;
    }

    public List<LogEntry> getLogDetailsByType(String filterType){
        log.info("Fetching log details for filter type: {}",filterType);
        try {
            List<LogEntry> logEntries = logRepository.findByType(filterType);
            if (logEntries.isEmpty()){
                log.warn("No log entries found for filter type: {}",filterType);
            }else {
                log.info("Found {} log entries for filter type: {}",logEntries.size(),filterType);
            }
            return logEntries;
        }catch (Exception e){
            log.error("Error occurred while fetching log details for filter type: {}",filterType,e);
            return Collections.emptyList();
        }
    }
}
