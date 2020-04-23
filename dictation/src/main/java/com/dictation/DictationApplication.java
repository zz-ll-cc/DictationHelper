package com.dictation;



import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableAsync
@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = "com.dictation.mapper")
public class DictationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DictationApplication.class, args);
    }

}
