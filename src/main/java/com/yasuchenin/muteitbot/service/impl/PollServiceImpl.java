package com.yasuchenin.muteitbot.service.impl;

import com.yasuchenin.muteitbot.configuration.BotConfigurationProperties;
import com.yasuchenin.muteitbot.dto.PollMuteDto;
import com.yasuchenin.muteitbot.service.MessageServiceApi;
import com.yasuchenin.muteitbot.service.PollServiceApi;
import com.yasuchenin.muteitbot.service.RestrictServiceApi;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.polls.StopPoll;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.polls.PollOption;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollServiceImpl implements PollServiceApi {

    private final static String YES_OPTION = "Да";
    private final static String NO_OPTION = "Нет";
    private final Map<String, PollMuteDto> pollMap = new ConcurrentHashMap<>();

    @Lazy
    private final MyBot myBot;
    private final BotConfigurationProperties config;
    private final MessageServiceApi messageServiceApi;
    private final RestrictServiceApi restrictServiceApi;

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS, initialDelay = 10)
    public void clearPollMap() {
        log.debug("clearing pool map");
        pollMap.entrySet().removeIf(
            entry -> ZonedDateTime.now().isAfter(entry.getValue().getUntilActive())
        );
    }

    @Override
    public void stopPoll(Integer messageId, Long chatId) {
        try {
            myBot.execute(
                StopPoll.builder()
                    .messageId(messageId)
                    .chatId(chatId)
                    .build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMutePoll(String userName, Long userId, long chatId, Integer days) {
        try {
            final SendPoll poll = SendPoll.builder()
                .chatId(chatId)
                .question("Замьютить %s на %s %s?".formatted(userName, days, convertDaysWord(days)))
                .option(YES_OPTION)
                .option(NO_OPTION)
                .isAnonymous(false)
                .build();
            final Message execute = myBot.execute(poll);
            pollMap.put(
                execute.getPoll().getId(),
                PollMuteDto.builder()
                    .chatId(chatId)
                    .pollMessageId(execute.getMessageId())
                    .userId(userId)
                    .userName(userName)
                    .daysMute(days)
                    .untilActive(
                        ZonedDateTime.now().plus(config.getVoteTillHours(), ChronoUnit.HOURS)
                    )
                    .build()
            );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkMutePollResults(Poll poll) {
        final String pollId = poll.getId();
        final PollMuteDto pollDto = pollMap.get(pollId);
        if (pollDto == null) {
            return;
        }
        long chatId = pollDto.getChatId();
        final Optional<PollOption> winnerResult = poll.getOptions().stream()
            .filter(pollOption -> pollOption.getVoterCount() >= config.getVoteWinnerCount())
            .findAny();
        if (winnerResult.isPresent()) {
            if (winnerResult.get().getText().equals(YES_OPTION)) {
                final Integer pollMessageId = pollDto.getPollMessageId();
                stopPoll(pollMessageId, chatId);
                try {
                    restrictServiceApi.muteUser(pollDto.getUserId(), chatId, pollDto.getDaysMute());
                } catch (TelegramApiException e) {
                    messageServiceApi.sendMsg("Я не смог - его сила больше моей((", chatId);
                    e.printStackTrace();
                    return;
                }
                messageServiceApi.sendMsg(
                    "Пользователь %s улетел в мьют на %s %s".formatted(
                        pollDto.getUserName(),
                        pollDto.getDaysMute(),
                        convertDaysWord(pollDto.getDaysMute())
                    ),
                    chatId
                );
            } else if (winnerResult.get().getText().equals(NO_OPTION)) {
                messageServiceApi.sendMsg(
                    "Пользователь %s увернулся от мьюта".formatted(
                        pollDto.getUserName()
                    ),
                    chatId
                );
                final Integer pollMessageId = pollDto.getPollMessageId();
                stopPoll(pollMessageId, chatId);
            }
            pollMap.remove(pollId);
        }
    }

    private String convertDaysWord(int count) {
        return count == 1 ? "сутки" : "суток";
    }

}
