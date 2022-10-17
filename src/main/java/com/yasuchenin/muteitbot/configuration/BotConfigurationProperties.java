package com.yasuchenin.muteitbot.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app.bot")
public class BotConfigurationProperties {

    private String token;

    private String botUserName;

    private Integer voteWinnerCount;

    private Long voteTillHours;
}