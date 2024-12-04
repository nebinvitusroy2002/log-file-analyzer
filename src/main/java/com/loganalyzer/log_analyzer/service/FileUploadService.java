package com.loganalyzer.log_analyzer.service;

import com.loganalyzer.log_analyzer.fileUploadUtil.FileUploadUtil;
import com.loganalyzer.log_analyzer.service.interfaces.FileUploadServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileUploadService implements FileUploadServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    @Override
    public String saveFile(MultipartFile multipartFile){
        String fileName = multipartFile.getOriginalFilename();
        logger.info("Starting file upload process for file: {}",fileName);
        try {
            String savedFilePath = FileUploadUtil.saveFile(fileName,multipartFile);
            logger.info("File uploaded successfully. File saved at: {}",savedFilePath);
            return savedFilePath;
        }catch (IOException e){
            logger.error("File upload failed for file: {}",fileName,e);
            throw new RuntimeException("File upload failed",e);
        }catch (Exception e){
            logger.error("Unexpected error occurred during file upload for file: {}",fileName,e);
            throw new RuntimeException("An unexpected error occurred during file upload",e);
        }
    }
}
