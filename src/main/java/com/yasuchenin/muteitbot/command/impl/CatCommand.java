package com.yasuchenin.muteitbot.command.impl;

import com.yasuchenin.muteitbot.command.BotCommand;
import com.yasuchenin.muteitbot.dto.RandomCatDto;
import com.yasuchenin.muteitbot.service.MessageServiceApi;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class CatCommand implements BotCommand {

    @Qualifier("catWebClient")
    private final WebClient webClient;
    private final MessageServiceApi messageServiceApi;

    @Override
    public void execute(Update update, List<String> messageCommands) {
        webClient.get().uri("https://aws.random.cat/meow")
            .retrieve()
            .bodyToMono(RandomCatDto.class)
            .subscribe(randomCatDto -> {
                messageServiceApi.sendMsg(randomCatDto.getFile(), update.getMessage().getChatId());
            });
    }

    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    public String getHelpMessage() {
        return null;
    }
}
