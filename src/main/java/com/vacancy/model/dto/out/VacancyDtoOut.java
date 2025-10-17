package com.vacancy.model.dto.out;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class VacancyDtoOut {
    private Long id;
    private String description;
    private String longDescription;
    private Integer minSalary;
    private Integer maxSalary;
    private String city;
}

