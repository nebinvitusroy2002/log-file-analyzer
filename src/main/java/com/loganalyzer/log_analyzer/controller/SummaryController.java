package com.loganalyzer.log_analyzer.controller;

import com.loganalyzer.log_analyzer.model.LogEntry;
import com.loganalyzer.log_analyzer.service.FileSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/log")
public class SummaryController {

    private final FileSummaryService fileSummaryService;

    public SummaryController(FileSummaryService fileSummaryService){
        this.fileSummaryService = fileSummaryService;
    }

    @GetMapping("/summary")
    public ResponseEntity<List<LogEntry>> getFilteredLogs(
            @RequestParam("filterType") String filterType){
        List<LogEntry> filteredLogs = fileSummaryService.getLogDetailsByType(filterType);
        return ResponseEntity.ok(filteredLogs);
    }
}
