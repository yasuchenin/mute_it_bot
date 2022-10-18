package com.yasuchenin.muteitbot.configuration;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import reactor.netty.http.client.HttpClient;

@Configuration
public class BotApiConfiguration {

    @Bean
    public TelegramBotsApi getTelegramBotsApi(TelegramLongPollingBot bot) throws TelegramApiException {
        new TelegramBotsApi(DefaultBotSession.class).registerBot(bot);

        return new TelegramBotsApi(DefaultBotSession.class);
    }

    @Bean
    public WebClient getWebClient(HttpClient httpClient, BotConfigurationProperties config) {
        return WebClient.builder()
            .baseUrl("https://api.unsplash.com")
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .defaultHeader("Authorization", "Bearer Client-ID %s".formatted(config.getUnsplashToken()))
            .build();
    }

    @Bean
    public HttpClient getHttpClient() {
        return HttpClient.create()
            .responseTimeout(Duration.ofSeconds(5));
    }

}
