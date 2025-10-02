package com.vacancy.controllers;

import com.vacancy.model.entities.User;
import com.vacancy.model.entities.Vacancy;
import com.vacancy.exceptions.RequestException;
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
            throw new RequestException(HttpStatus.NOT_FOUND, "Вакансия не найдена");
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
            throw new RequestException(HttpStatus.NOT_FOUND, "Вакансия не найдена");
        }
        Vacancy vacancy = vacancyDto.toVacancy();
        vacancy.setId(id);
        Vacancy updatedVacancy = vacancyRepository.save(vacancy);
        return ResponseEntity.ok(new VacancyDto(updatedVacancy));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long id) {
        vacancyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{vacancyId}/respond/{userId}")
    public ResponseEntity<Void> respondToVacancy(@PathVariable Long vacancyId, @PathVariable Long userId) {
        Optional<Vacancy> vacancyOpt = vacancyRepository.findById(vacancyId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (vacancyOpt.isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND, "Вакансия не найдена");
        }
        if (userOpt.isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }

        User user = userOpt.get();
        Vacancy vacancy = vacancyOpt.get();

        if (!user.getResponseList().contains(vacancy)) {
            user.getResponseList().add(vacancy);
            userRepository.save(user);
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{vacancyId}/respond/{userId}")
    public ResponseEntity<Void> removeResponseFromVacancy(@PathVariable Long vacancyId, @PathVariable Long userId) {
        Optional<Vacancy> vacancyOpt = vacancyRepository.findById(vacancyId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (vacancyOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.ok().build();
        }

        User user = userOpt.get();
        Vacancy vacancy = vacancyOpt.get();

        user.getResponseList().remove(vacancy);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{vacancyId}/favorite/{userId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable Long vacancyId, @PathVariable Long userId) {
        Optional<Vacancy> vacancyOpt = vacancyRepository.findById(vacancyId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (vacancyOpt.isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND, "Вакансия не найдена");
        }
        if (userOpt.isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }

        User user = userOpt.get();
        Vacancy vacancy = vacancyOpt.get();

        if (!user.getFavoriteList().contains(vacancy)) {
            user.getFavoriteList().add(vacancy);
            userRepository.save(user);
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{vacancyId}/favorite/{userId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long vacancyId, @PathVariable Long userId) {
        Optional<Vacancy> vacancyOpt = vacancyRepository.findById(vacancyId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (vacancyOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.ok().build();
        }

        User user = userOpt.get();
        Vacancy vacancy = vacancyOpt.get();

        user.getFavoriteList().remove(vacancy);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
