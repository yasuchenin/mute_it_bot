package com.yasuchenin.muteitbot.service;

public interface ClearMessageService {

    void saveUserMessageInfo(String userName, Integer messageId, Long chatId);

    int removeUserMessages(String userName, Long chatId);

}
