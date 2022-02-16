package com.psc.sample.r103;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class C003Application {

    public static void main(String[] args) {
        SpringApplication.run(C003Application.class, args);
    }

}
