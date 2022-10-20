package com.yasuchenin.muteitbot;

import com.yasuchenin.muteitbot.service.impl.PollServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.polls.PollOption;

@SpringBootTest
class MuteItBotApplicationTests {

    @Autowired
    PollServiceImpl pollServiceApi;

    @Test
    void contextLoads() {
    }

    @Test
    void testBigPool() {
        final Poll poll = new Poll();
        poll.setId("123");
        poll.setTotalVoterCount(20);
        final PollOption yes = new PollOption("Да", 17);
        final PollOption no = new PollOption("Нет", 3);
        poll.setOptions(List.of(yes, no));
        /*pollServiceApi.pollMap.put("123", PollMuteDto.builder()
            .chatId(1L).build());*/
        pollServiceApi.checkMutePollResults(poll);
    }

}
