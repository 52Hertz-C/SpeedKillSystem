package com.cxy.speedkill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Auther: cxy
 * @Date: 2019/7/19
 * @Description:
 */


@SpringBootApplication
@EnableScheduling
public class MainApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainApplication.class, args);
    }

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
//        return builder.sources(MainApplication.class);
//    }

}