package com.yasuchenin.muteitbot.service;

import java.util.List;

public interface MessageServiceApi {

    void sendMsg(String messageText, long chatId);

    void showHelp(long chatId);

    void showCommandList(List<String> commandNames, long chatId);

}
