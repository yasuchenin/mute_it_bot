package com.yasuchenin.muteitbot.service.impl;

import com.yasuchenin.muteitbot.command.BotCommand;
import com.yasuchenin.muteitbot.configuration.BotConfigurationProperties;
import com.yasuchenin.muteitbot.service.CommandHandler;
import com.yasuchenin.muteitbot.service.MessageServiceApi;
import com.yasuchenin.muteitbot.service.PollServiceApi;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class MyBot extends TelegramLongPollingBot {

    private final PollServiceApi pollServiceApi;
    private final MessageServiceApi messageServiceApi;
    private final CommandHandler commandHandler;

    private final BotConfigurationProperties config;

    @Override
    public String getBotUsername() {
        return config.getBotUserName();
    }

    @Override
    public String getBotToken() {
        return config.getTelegramToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()
            && update.getMessage().hasText()
            && update.getMessage().getText().startsWith(config.getBotUserName())
        ) {
            final List<String> messageCommands = getCommandList(update.getMessage().getText());
            if (messageCommands.size() <= 1) {
                messageServiceApi.showCommandList(commandHandler.getCommandsName(), update.getMessage().getChatId());
                return;
            }
            final String commandName = messageCommands.get(1);
            final BotCommand botCommand = commandHandler.getCommandByName(commandName);
            if (botCommand != null) {
                botCommand.execute(update, messageCommands);
                return;
            }
        }

        if (update.hasPoll() && !update.getPoll().getIsClosed()) {
            pollServiceApi.checkMutePollResults(update.getPoll());
        }
    }

    private List<String> getCommandList(String message) {
        return Arrays.stream(message.split("\\s+")).toList();
    }

}
