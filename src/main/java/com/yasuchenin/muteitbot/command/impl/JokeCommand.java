package com.yasuchenin.muteitbot.command.impl;

import com.yasuchenin.muteitbot.command.BotCommand;
import com.yasuchenin.muteitbot.configuration.BotConfigurationProperties;
import com.yasuchenin.muteitbot.service.MessageServiceApi;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class JokeCommand implements BotCommand {

    private final MessageServiceApi messageServiceApi;
    private final BotConfigurationProperties config;

    @Override
    public void execute(Update update, List<String> messageCommands) {
        messageServiceApi.sendMsg("%s дай анекдот".formatted(config.getAdminName()), update.getMessage().getChatId());
    }

    @Override
    public String getCommandName() {
        return "joke";
    }

    @Override
    public String getHelpMessage() {
        return "Формат команды: @mute_it_bot joke";
    }
}
