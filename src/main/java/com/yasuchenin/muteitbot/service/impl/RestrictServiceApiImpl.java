package com.yasuchenin.muteitbot.service.impl;

import com.yasuchenin.muteitbot.service.RestrictServiceApi;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RequiredArgsConstructor
@Service
public class RestrictServiceApiImpl implements RestrictServiceApi {

    @Lazy
    private final MyBot myBot;

    @Override
    public void muteUser(Long userId, long chatId, Integer days) throws TelegramApiException {
        final long muteUntilEpochSecond = ZonedDateTime.now().plus(days, ChronoUnit.DAYS)
            .toEpochSecond();
        final RestrictChatMember restrictChatMember = RestrictChatMember.builder()
            .chatId(chatId)
            .userId(userId)
            .untilDate((int) muteUntilEpochSecond)
            .permissions(
                ChatPermissions.builder()
                    .canSendMessages(false)
                    .build()
            )
            .build();
        myBot.execute(restrictChatMember);
    }

}
