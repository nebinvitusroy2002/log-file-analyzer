package com.loganalyzer.log_analyzer.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadServiceInterface {
    public String saveFile(MultipartFile multipartFile);
}
