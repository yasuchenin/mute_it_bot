package com.yasuchenin.muteitbot.configuration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
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
    public WebClient unsplashWebClient(HttpClient httpClient, BotConfigurationProperties config) {
        return WebClient.builder()
            .baseUrl("https://api.unsplash.com")
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .defaultHeader("Authorization", "Bearer Client-ID %s".formatted(config.getUnsplashToken()))
            .build();
    }

    @Bean
    public WebClient yandexWebClient(HttpClient httpClient) {
        return WebClient.builder()
            .baseUrl("https://yandex.ru/lab/api/yalm/text3")
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    @Bean
    public WebClient rzhunemoguWebClient(HttpClient httpClient, BotConfigurationProperties config) {
        return WebClient.builder()
            .baseUrl("http://rzhunemogu.ru")
            .defaultHeader("Accept-Encoding", "windows-1251")
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    @Bean
    public WebClient catWebClient(HttpClient httpClient) {
        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    @Bean
    public HttpClient getHttpClient() {
        return HttpClient.create()
            .responseTimeout(Duration.ofSeconds(15));
    }

    @Bean
    public Random getRandom() {
        return new Random(LocalDateTime.now().getNano());
    }

}
