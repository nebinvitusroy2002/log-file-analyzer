package com.loganalyzer.log_analyzer.service;

import com.loganalyzer.log_analyzer.fileDownloadUtil.FileDownloadUtil;
import com.loganalyzer.log_analyzer.model.LogEntry;
import com.loganalyzer.log_analyzer.repository.LogRepository;
import com.loganalyzer.log_analyzer.service.interfaces.FileFilterServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileFilterService implements FileFilterServiceInterface {
    
    private static final Logger logger = LoggerFactory.getLogger(FileFilterService.class);

    private final FileDownloadUtil fileDownloadUtil;
    private final LogRepository logRepository;

    public FileFilterService(FileDownloadUtil fileDownloadUtil, LogRepository logRepository) {
        this.fileDownloadUtil = fileDownloadUtil;
        this.logRepository = logRepository;
    }

    public void filterAndSaveLogs(String fileCode,String filterType) {
        logger.info("Starting the log filtering for fileCode: {}, filterType: {}",fileCode,filterType);
        Path logFilePath = null;
        try {
            logFilePath = fileDownloadUtil.getFileByCode(fileCode);
        }catch (Exception e){
            logger.error("Error retrieving file with code {}: {}",fileCode,e.getMessage());
            return;
        }
        if (logFilePath == null){
            logger.error("Log file path is null for the file code: {}",fileCode);
            return;
        }
        File logfile = logFilePath.toFile();
        if (!logfile.exists() || !logfile.isFile()){
            logger.error("Log file not found {}",logFilePath);
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
            logger.info("Successfully saves {} log entries of type: {}",filteredLogs.size(),filterType);
        }catch (IOException e){
            logger.error("Error reading log file: {}",logFilePath,e);
        }catch (Exception e){
            logger.error("Unexpected error occurred while processing the log file: {}",logFilePath,e);
        }
    }

}
