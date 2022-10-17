package com.yasuchenin.muteitbot.service.impl;

import com.yasuchenin.muteitbot.command.BotCommand;
import com.yasuchenin.muteitbot.service.CommandHandler;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandHandlerImpl implements CommandHandler {

    private Map<String, BotCommand> botCommandMap;

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
    public BotCommand getCommandByName(String name) {
        return botCommandMap.get(name);
    }

    @Override
    public List<String> getCommandsName() {
        return botCommandMap.values().stream().map(
            BotCommand::getCommandName
        ).toList();
    }

}
