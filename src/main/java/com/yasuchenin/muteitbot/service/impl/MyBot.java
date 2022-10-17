package com.yasuchenin.muteitbot.service.impl;

import com.yasuchenin.muteitbot.command.BotCommand;
import com.yasuchenin.muteitbot.configuration.BotConfigurationProperties;
import com.yasuchenin.muteitbot.service.MessageServiceApi;
import com.yasuchenin.muteitbot.service.PollServiceApi;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class MyBot extends TelegramLongPollingBot {

    private final PollServiceApi pollServiceApi;
    private final MessageServiceApi messageServiceApi;

    private Map<String, BotCommand> botCommandMap;

    private final BotConfigurationProperties config;

    @Autowired
    public void initCommandMap(List<BotCommand> botCommands) {
        botCommandMap = new ConcurrentHashMap<>();
        botCommands.forEach(botCommand -> {
            if (botCommandMap.containsKey(botCommand.getCommandName())) {
                throw new IllegalStateException(
                    "Duplicate command name - %s".formatted(botCommand.getCommandName())
                );
            }
            botCommandMap.put(botCommand.getCommandName(), botCommand);
        });
    }

    @Override
    public String getBotUsername() {
        return config.getBotUserName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()
            && update.getMessage().hasText()
            && update.getMessage().getText().startsWith(config.getBotUserName())
        ) {
            final List<String> messageCommands = getCommandList(update.getMessage().getText());
            if (messageCommands.size() <= 1) {
                final List<String> commandNames = botCommandMap.values().stream().map(
                    BotCommand::getCommandName
                ).toList();
                messageServiceApi.showCommandList(commandNames, update.getMessage().getChatId());
                return;
            }
            final String commandName = messageCommands.get(1);
            final BotCommand botCommand = botCommandMap.get(commandName);
            if (botCommand != null) {
                botCommand.execute(update, messageCommands);
                return;
            }
        }

        if (update.hasPoll()) {
            pollServiceApi.checkMutePollResults(update.getPoll());
        }
    }

    private List<String> getCommandList(String message) {
        return Arrays.stream(message.split("\\s+")).toList();
    }

}
