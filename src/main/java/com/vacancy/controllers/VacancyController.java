package com.vacancy.controllers;

import com.vacancy.model.entities.Vacancy;
import com.vacancy.model.dto.VacancyDto;
import com.vacancy.service.VacancyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyService vacancyService;

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

    @GetMapping("/{id}")
    public ResponseEntity<VacancyDto> getVacancyById(@PathVariable Long id) {
        Vacancy vacancy = vacancyService.getVacancyById(id);
        return ResponseEntity.ok(new VacancyDto(vacancy));
    }

    @PostMapping
    public ResponseEntity<VacancyDto> createVacancy(@Valid @RequestBody VacancyDto vacancyDto) {
        Vacancy savedVacancy = vacancyService.createVacancy(vacancyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new VacancyDto(savedVacancy));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VacancyDto> updateVacancy(@PathVariable Long id, @Valid @RequestBody VacancyDto vacancyDto) {
        Vacancy updatedVacancy = vacancyService.updateVacancy(id, vacancyDto);
        return ResponseEntity.ok(new VacancyDto(updatedVacancy));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long id) {
        vacancyService.deleteVacancy(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{vacancyId}/respond/{userId}")
    public ResponseEntity<Void> respondToVacancy(@PathVariable Long vacancyId, @PathVariable Long userId) {
        vacancyService.respondToVacancy(vacancyId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{vacancyId}/respond/{userId}")
    public ResponseEntity<Void> removeResponseFromVacancy(@PathVariable Long vacancyId, @PathVariable Long userId) {
        vacancyService.removeResponseFromVacancy(vacancyId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{vacancyId}/favorite/{userId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable Long vacancyId, @PathVariable Long userId) {
        vacancyService.addToFavorites(vacancyId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{vacancyId}/favorite/{userId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long vacancyId, @PathVariable Long userId) {
        vacancyService.removeFromFavorites(vacancyId, userId);
        return ResponseEntity.ok().build();
    }
}
