package com.yasuchenin.muteitbot.service;

public interface RestrictServiceApi {

    void muteUser(Long userId, long chatId, Integer days);

}
