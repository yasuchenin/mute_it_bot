package com.yasuchenin.muteitbot.service.impl;

import com.yasuchenin.muteitbot.infrastructure.UserMessageEntity;
import com.yasuchenin.muteitbot.infrastructure.UserMessagesRepo;
import com.yasuchenin.muteitbot.service.ClearMessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class ClearMessageApiImpl implements ClearMessageService {

    private final UserMessagesRepo userMessagesRepo;

    @Lazy
    private final MyBot myBot;

    @Override
    @Transactional
    public void saveUserMessageInfo(String userName, Integer messageId, Long chatId) {
        final UserMessageEntity messageEntity = UserMessageEntity.builder()
            .userName(userName)
            .messageId(messageId)
            .chatId(chatId)
            .build();
        userMessagesRepo.save(messageEntity);
    }

    @Override
    @Transactional
    public int removeUserMessages(String userName, Long chatId) {
        final List<Integer> userMessages = userMessagesRepo.findByUserNameAndChatId(userName, chatId)
            .stream()
            .map(UserMessageEntity::getMessageId)
            .toList();

        userMessages.forEach(messageId -> removeUserMessage(chatId, messageId));
        userMessagesRepo.deleteByUserName(userName);

        return userMessages.size();
    }

    private void removeUserMessage(Long chatId, Integer messageId) {
        try {
            myBot.execute(DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build());
        } catch (TelegramApiException e) {
            System.out.println("removeUserMessage er " + e.getMessage());
        }
    }


}
