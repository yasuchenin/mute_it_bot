package com.yasuchenin.muteitbot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotApiConfiguration {

    @Bean
    public TelegramBotsApi getTelegramBotsApi(TelegramLongPollingBot bot) throws TelegramApiException {
        new TelegramBotsApi(DefaultBotSession.class).registerBot(bot);

        return new TelegramBotsApi(DefaultBotSession.class);
    }
}
