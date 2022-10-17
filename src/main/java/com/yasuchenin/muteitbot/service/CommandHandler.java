package com.yasuchenin.muteitbot.service;

import com.yasuchenin.muteitbot.command.BotCommand;
import java.util.List;

public interface CommandHandler {

    BotCommand getCommandByName(String name);

    List<String> getCommandsName();
}
