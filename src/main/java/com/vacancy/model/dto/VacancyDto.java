package com.vacancy.model.dto;


import com.vacancy.model.entities.Vacancy;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VacancyDto {

    public VacancyDto(Vacancy vacancy) {

    }

    public Vacancy toVacancy() {

    }
}
