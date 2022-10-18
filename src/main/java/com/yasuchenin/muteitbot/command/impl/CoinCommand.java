package com.yasuchenin.muteitbot.command.impl;

import com.yasuchenin.muteitbot.command.BotCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class CoinCommand implements BotCommand {

    @Override
    public void execute(Update update, List<String> messageCommands) {

    }

    @Override
    public String getCommandName() {
        return "coin";
    }

    @Override
    public String getHelpMessage() {
        return null;
    }
}
