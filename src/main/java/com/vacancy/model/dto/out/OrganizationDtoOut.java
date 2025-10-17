package com.vacancy.model.dto.out;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OrganizationDtoOut (
    Long id,
    String nickname,
    String email
){}
