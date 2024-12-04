package com.loganalyzer.log_analyzer.controller;

import com.loganalyzer.log_analyzer.dto.FilterRequest;
import com.loganalyzer.log_analyzer.service.FileFilterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/log")
public class FilterController {

    private final FileFilterService fileFilterService;

    public FilterController(FileFilterService fileFilterService) {
        this.fileFilterService = fileFilterService;
    }

    @PostMapping("/filter")
    public ResponseEntity<String> filterLogs(
            @RequestBody FilterRequest filterRequest){
        try {
            fileFilterService.filterAndSaveLogs(filterRequest.getFileCode(),filterRequest.getFilterType());
            return ResponseEntity.ok("Filtered logs saved to the database.");
        }catch (IOException e){
            return ResponseEntity.status(500).body("Error processing the log file: "+e.getMessage());
        }
    }
}
