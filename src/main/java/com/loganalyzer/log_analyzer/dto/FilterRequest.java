package com.loganalyzer.log_analyzer.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FilterRequest {
    private String fileCode;
    private String filterType;

}
