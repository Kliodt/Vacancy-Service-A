package com.vacancy.model.dto.out;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserVacancyResponseDtoOut (
    Long userId,
    Long vacancyId,
    OffsetDateTime responseDate
){}
