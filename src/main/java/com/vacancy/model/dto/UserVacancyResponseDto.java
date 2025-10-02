package com.vacancy.model.dto;

import java.time.OffsetDateTime;

import com.vacancy.model.entities.UserVacancyResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserVacancyResponseDto {
    
    private Long id;
    private Long userId;
    private Long vacancyId;
    private OffsetDateTime responseDate;

    public UserVacancyResponseDto(UserVacancyResponse userVacancyResponse) {
        this.id = userVacancyResponse.getId();
        this.userId = userVacancyResponse.getUser().getId();
        this.vacancyId = userVacancyResponse.getVacancy().getId();
        this.responseDate = userVacancyResponse.getResponseDate();
    }
}