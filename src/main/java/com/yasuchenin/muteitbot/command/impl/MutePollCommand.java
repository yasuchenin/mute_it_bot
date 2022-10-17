package com.yasuchenin.muteitbot.command.impl;

import com.yasuchenin.muteitbot.command.BotCommand;
import com.yasuchenin.muteitbot.service.PollServiceApi;
import com.yasuchenin.muteitbot.service.MessageServiceApi;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class MutePollCommand implements BotCommand {

    private final MessageServiceApi messageServiceApi;
    private final PollServiceApi pollServiceApi;

    private final Pattern isNumericPattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    @Override
    public void execute(Update update, List<String> messageCommands) {
        long chatId = update.getMessage().getChatId();

        if (update.getMessage().isReply()) {
            final Long muteUserId = update.getMessage().getReplyToMessage().getFrom().getId();
            final String muteUserName = update.getMessage().getReplyToMessage().getFrom().getUserName();

            if (messageCommands.size() < 2 || !isNumeric(messageCommands.get(1))) {
                messageServiceApi.sendMsg("Некорректный формат команды mute", chatId);
            }
            pollServiceApi.sendMutePoll(muteUserName, muteUserId, chatId, Integer.valueOf(messageCommands.get(1)));
        }

    }

    @Override
    public String getCommandName() {
        return "mute";
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return isNumericPattern.matcher(strNum).matches();
    }

}
