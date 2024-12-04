package com.loganalyzer.log_analyzer.service;

import com.loganalyzer.log_analyzer.fileDownloadUtil.FileDownloadUtil;
import com.loganalyzer.log_analyzer.model.LogEntry;
import com.loganalyzer.log_analyzer.repository.LogRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileFilterService {

    private final FileDownloadUtil fileDownloadUtil;
    private final LogRepository logRepository;

    public FileFilterService(FileDownloadUtil fileDownloadUtil, LogRepository logRepository) {
        this.fileDownloadUtil = fileDownloadUtil;
        this.logRepository = logRepository;
    }

    public void filterAndSaveLogs(String fileCode,String filterType) throws IOException{
        Path logFilePath = fileDownloadUtil.getFileByCode(fileCode);
        File logfile = logFilePath.toFile();

        List<LogEntry> filteredLogs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(logfile))){
            String line;
            while ((line = reader.readLine())!=null){
                if (line.contains(filterType)){
                    LogEntry logEntry = new LogEntry();
                    logEntry.setType(filterType);
                    logEntry.setMessage(line);
                    filteredLogs.add(logEntry);
                }
            }
        }
        logRepository.saveAll(filteredLogs);
    }

}