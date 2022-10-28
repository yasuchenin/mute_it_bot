package com.yasuchenin.muteitbot.service;

public interface ClearMessageService {

    void saveUserMessageInfo(String userName, Integer messageId, Long chatId, Long userId);

    int removeUserMessages(String userName, Long chatId);

}
