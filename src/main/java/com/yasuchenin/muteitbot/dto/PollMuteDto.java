package com.yasuchenin.muteitbot.dto;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PollMuteDto {

    private Long userId;

    private String userName;

    private int daysMute;

    private Long chatId;

    private Integer pollMessageId;

    private ZonedDateTime untilActive;

}
