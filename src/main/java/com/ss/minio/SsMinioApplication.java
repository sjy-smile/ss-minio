package com.ss.minio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class SsMinioApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsMinioApplication.class, args);
    }

}
