package com.vacancy.controllers;

import com.vacancy.model.entities.User;
import com.vacancy.model.entities.Vacancy;
import com.vacancy.exceptions.BadRequestException;
import com.vacancy.model.dto.VacancyDto;
import com.vacancy.repository.UserRepository;
import com.vacancy.repository.VacancyRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vacancies")
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyRepository vacancyRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<VacancyDto>> getAllVacancies() {
        List<VacancyDto> dtos = ((List<Vacancy>) vacancyRepository.findAll()).stream().map(VacancyDto::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacancyDto> getVacancyById(@PathVariable Long id) {
        Optional<Vacancy> vacancy = vacancyRepository.findById(id);
        if (vacancy.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new VacancyDto(vacancy.get()));
    }

    @PostMapping
    public ResponseEntity<VacancyDto> createVacancy(@Valid @RequestBody VacancyDto vacancyDto) {
        Vacancy vacancy = vacancyDto.toVacancy();
        Vacancy savedVacancy = vacancyRepository.save(vacancy);
        return ResponseEntity.status(HttpStatus.CREATED).body(new VacancyDto(savedVacancy));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VacancyDto> updateVacancy(@PathVariable Long id, @Valid @RequestBody VacancyDto vacancyDto) {
        if (!vacancyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Vacancy vacancy = vacancyDto.toVacancy();
        vacancy.setId(id);
        Vacancy updatedVacancy = vacancyRepository.save(vacancy);
        return ResponseEntity.ok(new VacancyDto(updatedVacancy));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long id) {
        if (!vacancyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vacancyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{vacancyId}/respond/{userId}")
    public ResponseEntity<String> respondToVacancy(@PathVariable Long vacancyId, @PathVariable Long userId) {
        Optional<Vacancy> vacancyOpt = vacancyRepository.findById(vacancyId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (vacancyOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        Vacancy vacancy = vacancyOpt.get();

        if (user.getResponseList().contains(vacancy)) {
            throw new BadRequestException("Уже откликался ранее");
        }

        user.getResponseList().add(vacancy);
        userRepository.save(user);

        return ResponseEntity.ok("Отклик осуществлен");
    }

    @DeleteMapping("/{vacancyId}/respond/{userId}")
    public ResponseEntity<String> removeResponseFromVacancy(@PathVariable Long vacancyId, @PathVariable Long userId) {
        Optional<Vacancy> vacancyOpt = vacancyRepository.findById(vacancyId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (vacancyOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        Vacancy vacancy = vacancyOpt.get();

        user.getResponseList().remove(vacancy);
        userRepository.save(user);

        return ResponseEntity.ok("Отклик на вакансию удален");
    }

    @PostMapping("/{vacancyId}/favorite/{userId}")
    public ResponseEntity<String> addToFavorites(@PathVariable Long vacancyId, @PathVariable Long userId) {
        Optional<Vacancy> vacancyOpt = vacancyRepository.findById(vacancyId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (vacancyOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        Vacancy vacancy = vacancyOpt.get();

        if (user.getFavoriteList().contains(vacancy)) {
            throw new BadRequestException("Вакансия уже добавлена в избранное");
        }

        user.getFavoriteList().add(vacancy);
        userRepository.save(user);

        return ResponseEntity.ok("Вакансия добавлена в избранное");
    }

    @DeleteMapping("/{vacancyId}/favorite/{userId}")
    public ResponseEntity<String> removeFromFavorites(@PathVariable Long vacancyId, @PathVariable Long userId) {
        Optional<Vacancy> vacancyOpt = vacancyRepository.findById(vacancyId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (vacancyOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        Vacancy vacancy = vacancyOpt.get();

        user.getFavoriteList().remove(vacancy);
        userRepository.save(user);

        return ResponseEntity.ok("Вакансия удалена из избранного");
    }
}
