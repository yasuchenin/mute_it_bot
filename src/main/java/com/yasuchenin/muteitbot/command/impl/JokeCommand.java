package com.yasuchenin.muteitbot.command.impl;

import com.yasuchenin.muteitbot.command.BotCommand;
import com.yasuchenin.muteitbot.infrastructure.AnekEntity;
import com.yasuchenin.muteitbot.infrastructure.AnekRepo;
import com.yasuchenin.muteitbot.service.MessageServiceApi;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class JokeCommand implements BotCommand {

    private final MessageServiceApi messageServiceApi;
    private final AnekRepo anekRepo;
    private final Random random;

    @Override
    public void execute(Update update, List<String> messageCommands) {
        final long anekCount = anekRepo.count();
        final long nextAnekId = random.nextLong(0, anekCount - 1);
        final AnekEntity anekEntity = anekRepo.findById(nextAnekId).orElseThrow();

        messageServiceApi.sendMsg(anekEntity.getContent(), update.getMessage().getChatId());
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
