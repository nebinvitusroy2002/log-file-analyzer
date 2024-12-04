package com.loganalyzer.log_analyzer.controller;

import com.loganalyzer.log_analyzer.response.FileUploadResponse;
import com.loganalyzer.log_analyzer.service.FileUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/log")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService){
       this.fileUploadService = fileUploadService;
    }
    @PostMapping("/uploadFile")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file")MultipartFile multipartFile){
        String fileCode = fileUploadService.saveFile(multipartFile);
        FileUploadResponse response = new FileUploadResponse();
        response.setFileName(multipartFile.getOriginalFilename());
        response.setSize(multipartFile.getSize());
        response.setDownloadUrl("/download/downloadFile/"+fileCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
