package com.loganalyzer.log_analyzer.service;

import com.loganalyzer.log_analyzer.exceptions.FileProcessingException;
import com.loganalyzer.log_analyzer.fileUploadUtil.FileUploadUtil;
import com.loganalyzer.log_analyzer.service.interfaces.FileUploadServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@Slf4j
public class FileUploadService implements FileUploadServiceInterface {

    public String saveFile(MultipartFile multipartFile){
        String fileName = multipartFile.getOriginalFilename();
        log.info("Starting file upload process for file: {}",fileName);
        try {
            String savedFilePath = FileUploadUtil.saveFile(fileName,multipartFile);
            log.info("File uploaded successfully. File saved at: {}",savedFilePath);
            return savedFilePath;
        }catch (IOException e){
            log.error("File upload failed for file: {}",fileName,e);
            throw new FileProcessingException("File Upload Failed: "+fileName,e);
        }
    }
}
