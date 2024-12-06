package com.loganalyzer.log_analyzer.service;

import com.loganalyzer.log_analyzer.exceptions.FileProcessingException;
import com.loganalyzer.log_analyzer.fileDownloadUtil.FileDownloadUtil;
import com.loganalyzer.log_analyzer.model.LogEntry;
import com.loganalyzer.log_analyzer.repository.LogRepository;
import com.loganalyzer.log_analyzer.service.interfaces.FileFilterServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FileFilterService implements FileFilterServiceInterface {

    private final FileDownloadUtil fileDownloadUtil;
    private final LogRepository logRepository;

    public FileFilterService(FileDownloadUtil fileDownloadUtil, LogRepository logRepository) {
        this.fileDownloadUtil = fileDownloadUtil;
        this.logRepository = logRepository;
    }

    public void filterAndSaveLogs(String fileCode,String filterType) {
        log.info("Starting the log filtering for fileCode: {}, filterType: {}",fileCode,filterType);
        Path logFilePath = null;
        try {
            logFilePath = fileDownloadUtil.getFileByCode(fileCode);
        }catch (Exception e){
            log.error("Error retrieving file with code {}: {}",fileCode,e.getMessage());
            throw new FileProcessingException("Error retrieving file with code: "+fileCode,e);
        }
        if (logFilePath == null){
            throw new FileProcessingException("Log file path is null for the file code: "+fileCode);
        }
        File logfile = logFilePath.toFile();
        if (!logfile.exists() || !logfile.isFile()){
            throw new FileProcessingException("Log file not found at: "+logFilePath);
        }

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
            logRepository.saveAll(filteredLogs);
            log.info("Successfully saves {} log entries of type: {}",filteredLogs.size(),filterType);
        }catch (IOException e){
            log.error("Error reading log file: {}",logFilePath,e);
            throw new FileProcessingException("Error reading log file: "+logFilePath,e);
        }
    }

}
