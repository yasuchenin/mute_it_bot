package com.yasuchenin.muteitbot.command.impl;

import com.yasuchenin.muteitbot.command.BotCommand;
import com.yasuchenin.muteitbot.dto.YandexRequestDto;
import com.yasuchenin.muteitbot.dto.YandexResponseDto;
import com.yasuchenin.muteitbot.service.MessageServiceApi;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RandomJokeCommand implements BotCommand {

    @Qualifier("yandexWebClient")
    private final WebClient webClient;
    private final MessageServiceApi messageServiceApi;

    @Override
    public void execute(Update update, List<String> messageCommands) {
        final Long chatId = update.getMessage().getChatId();
        if (messageCommands.size() <= 2) {
            messageServiceApi.sendMsg(getHelpMessage(), chatId);
            return;
        }

        final String jokeQuery = messageCommands.subList(2, messageCommands.size()).stream()
            .reduce((x, y) -> x.concat(" ").concat(y))
            .orElseThrow();
        final YandexRequestDto yandexRequestDto = new YandexRequestDto(jokeQuery);

        final Mono<YandexResponseDto> monoResponse = webClient.post().body(Mono.just(yandexRequestDto), YandexRequestDto.class)
            .retrieve()
            .bodyToMono(YandexResponseDto.class);

        monoResponse.subscribe(response -> {
            if (response != null && response.getQuery() != null
                && response.getText() != null && !"".equals(response.getText())) {
                messageServiceApi.sendMsg(response.getQuery() + response.getText(), chatId);
            }
        });
    }

    @Override
    public String getCommandName() {
        return "random_joke";
    }

    @Override
    public String getHelpMessage() {
        return "Формат команды: @mute_it_bot random_joke <ваш текст>. Пример: @mute_it_bot random_joke заходит еврей в бар";
    }
}
