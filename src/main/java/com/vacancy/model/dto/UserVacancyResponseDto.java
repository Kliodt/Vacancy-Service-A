package com.vacancy.model.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vacancy.model.entities.UserVacancyResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserVacancyResponseDto {
    
    private Long userId;
    private Long vacancyId;
    private OffsetDateTime responseDate;

    public UserVacancyResponseDto(UserVacancyResponse userVacancyResponse) {
        this.userId = userVacancyResponse.getUser().getId();
        this.vacancyId = userVacancyResponse.getVacancy().getId();
        this.responseDate = userVacancyResponse.getResponseDate();
    }
}