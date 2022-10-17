package com.yasuchenin.muteitbot.service;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface RestrictServiceApi {

    void muteUser(Long userId, long chatId, Integer days) throws TelegramApiException;

}
