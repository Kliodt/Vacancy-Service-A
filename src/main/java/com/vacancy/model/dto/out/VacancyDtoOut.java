package com.vacancy.model.dto.out;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record VacancyDtoOut (
    Long id,
    String description,
    String longDescription,
    Integer minSalary,
    Integer maxSalary,
    String city
){}
