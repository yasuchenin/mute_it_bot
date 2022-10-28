package com.yasuchenin.muteitbot.command.impl;

import com.yasuchenin.muteitbot.command.BotCommand;
import com.yasuchenin.muteitbot.configuration.BotConfigurationProperties;
import com.yasuchenin.muteitbot.dto.UnsplashResponse;
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
public class NudesCommand implements BotCommand {

    private static final String PIC_SIZE = "regular";

    @Qualifier("unsplashWebClient")
    private final WebClient webClient;
    private final MessageServiceApi messageServiceApi;
    private final BotConfigurationProperties config;

    @Override
    public void execute(Update update, List<String> messageCommands) {
        Mono<UnsplashResponse> monoResponse = webClient.get().uri(
                uriBuilder -> uriBuilder
                    .path("/photos/random")
                    .queryParam("query", config.getUnsplashQuery())
                    .build()
            )
            .retrieve()
            .bodyToMono(UnsplashResponse.class);

        monoResponse.subscribe(response -> {
            if (response != null && response.getUrls() != null && response.getUrls().get(PIC_SIZE) != null) {
                messageServiceApi.sendMsg(response.getUrls().get(PIC_SIZE), update.getMessage().getChatId());
            }
        });

    }

    @Override
    public String getCommandName() {
        return "nudes";
    }

    @Override
    public String getHelpMessage() {
        return "Формат команды: @mute_it_bot joke";
    }
}
