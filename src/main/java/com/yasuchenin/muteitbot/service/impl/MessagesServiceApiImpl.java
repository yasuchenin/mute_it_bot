package com.yasuchenin.muteitbot.service.impl;

import com.yasuchenin.muteitbot.service.MessageServiceApi;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class MessagesServiceApiImpl implements MessageServiceApi {

    @Lazy
    private final MyBot myBot;

    @Override
    public void sendMsg(String messageText, long chatId) {
        try {
            final SendMessage qwe = SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .build();
            myBot.execute(qwe);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showCommandList(List<String> commandNames, long chatId) {
        final Optional<String> reduce = commandNames.stream()
            .reduce((x, y) -> x.concat(", ").concat(y));
        sendMsg("Доступные команды: " + reduce.orElse("none"), chatId);
    }

}
