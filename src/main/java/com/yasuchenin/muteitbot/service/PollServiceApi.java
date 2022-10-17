package com.yasuchenin.muteitbot.service;

import org.telegram.telegrambots.meta.api.objects.polls.Poll;

public interface PollServiceApi {

    void sendMutePoll(String userName, Long userId, long chatId, Integer days);

    void stopPoll(Integer messageId, Long chatId);

    void checkMutePollResults(Poll poll);

}
