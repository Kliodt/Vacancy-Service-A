package com.vacancy.controllers;


import com.vacancy.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyRepository vacancyRepository;

    public void addVacancy() {

    }
}
