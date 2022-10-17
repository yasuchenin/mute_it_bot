package com.yasuchenin.muteitbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MuteItBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(MuteItBotApplication.class, args);
    }

}
