package com.yasuchenin.muteitbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YandexRequestDto {

    private String query;
    private int intro = 0;
    private int filter = 1;

    public YandexRequestDto(String query) {
        this.query = query;
    }

}
