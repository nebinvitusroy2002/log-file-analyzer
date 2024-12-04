package com.loganalyzer.log_analyzer.service.interfaces;

import java.io.IOException;

public interface FileFilterServiceInterface {
    void filterAndSaveLogs(String fileCode,String filterType) throws IOException;
}
