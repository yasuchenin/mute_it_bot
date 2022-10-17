package com.yasuchenin.muteitbot.command.impl;

import com.yasuchenin.muteitbot.command.BotCommand;
import com.yasuchenin.muteitbot.service.MessageServiceApi;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class HelpCommand implements BotCommand {

    private final MessageServiceApi messageServiceApi;

    @Override
    public void execute(Update update, List<String> messageCommands) {
        messageServiceApi.showHelp(update.getMessage().getChatId());
    }

    @Override
    public String getCommandName() {
        return "help";
    }
}
