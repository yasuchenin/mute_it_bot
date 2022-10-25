package com.yasuchenin.muteitbot.command.impl;

import com.yasuchenin.muteitbot.command.BotCommand;
import com.yasuchenin.muteitbot.configuration.BotConfigurationProperties;
import com.yasuchenin.muteitbot.service.ClearMessageService;
import com.yasuchenin.muteitbot.service.MessageServiceApi;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class ClearMessageCommand implements BotCommand {

    private final ClearMessageService clearMessageService;
    private final MessageServiceApi messageServiceApi;
    private final BotConfigurationProperties config;

    @Override
    public void execute(Update update, List<String> messageCommands) {
        final String senderName = update.getMessage().getFrom().getUserName();
        if (!config.getAdminName().equals(senderName)) {
            messageServiceApi.sendMsg("Нет. Иди попроси Стаса об этом.", update.getMessage().getChatId());
            return;
        }
        if (messageCommands.size() < 3) {
            messageServiceApi.sendMsg(getHelpMessage(), update.getMessage().getChatId());
            return;
        }

        final int count = clearMessageService.removeUserMessages(
            messageCommands.get(2), update.getMessage().getChatId()
        );
        messageServiceApi.sendMsg("Удалено %s сообщений".formatted(count), update.getMessage().getChatId());
    }

    @Override
    public String getCommandName() {
        return "clear";
    }

    @Override
    public String getHelpMessage() {
        return "Формат команды: @mute_it_bot clear @username";
    }
}
