package com.yasuchenin.muteitbot.command.impl;

import com.yasuchenin.muteitbot.command.BotCommand;
import com.yasuchenin.muteitbot.service.MessageServiceApi;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class ComplaintCommand implements BotCommand {

    private final MessageServiceApi messageServiceApi;

    @Override
    public void execute(Update update, List<String> messageCommands) {
        if (messageCommands.size() < 3) {
            messageServiceApi.sendMsg(getHelpMessage(), update.getMessage().getChatId());
            return;
        }
        messageServiceApi.sendMsg("Ваша жалоба принята, спасибо за обращение!", update.getMessage().getChatId());
    }

    @Override
    public String getCommandName() {
        return "complaint";
    }

    @Override
    public String getHelpMessage() {
        return "Формат команды: @mute_it_bot complaint <жалоба к админу>";
    }
}
