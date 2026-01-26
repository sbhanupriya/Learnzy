package com.learnzy.backend.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {

    @Value("${spring.seed-data.file-path}")
    private String filePath;

    public String getFilePath(){
        return filePath;
    }
}
