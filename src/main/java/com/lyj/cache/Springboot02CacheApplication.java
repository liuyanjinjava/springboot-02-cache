package com.lyj.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan("com.lyj.cache.mapper")
@EnableCaching
@SpringBootApplication
public class Springboot02CacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot02CacheApplication.class, args);
    }
}
