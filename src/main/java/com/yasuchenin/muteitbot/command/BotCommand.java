package com.yasuchenin.muteitbot.command;

import java.util.List;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotCommand {

    void execute(Update update, List<String> messageCommands);

    String getCommandName();

}
