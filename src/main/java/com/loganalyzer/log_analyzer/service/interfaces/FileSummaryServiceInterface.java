package com.loganalyzer.log_analyzer.service.interfaces;

import com.loganalyzer.log_analyzer.model.LogEntry;

import java.util.List;

public interface FileSummaryServiceInterface {
    public List<LogEntry> getLogDetailsByType(String filterType);
}
