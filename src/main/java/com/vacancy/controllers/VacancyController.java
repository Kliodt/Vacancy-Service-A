package com.vacancy.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vacancy.model.dto.VacancyDto;
import com.vacancy.model.entities.Vacancy;
import com.vacancy.service.VacancyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vacancies")
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyService vacancyService;

    @Operation(
            summary = "Получить все вакансии",
            description = "Возвращает список вакансий страницами"
    )
    @GetMapping
    public ResponseEntity<List<VacancyDto>> getAllVacancies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        Page<Vacancy> vacancyPage = vacancyService.getAllVacancies(page, size);
        
        List<VacancyDto> dtos = vacancyPage.getContent().stream().map(VacancyDto::new).toList();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(vacancyPage.getTotalElements()));
        
        return ResponseEntity.ok().headers(headers).body(dtos);
    }
}
