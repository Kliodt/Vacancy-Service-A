package com.vacancy.controllers;

import com.vacancy.model.entities.User;
import com.vacancy.model.entities.Vacancy;
import com.vacancy.model.dto.UserDto;
import com.vacancy.model.dto.UserVacancyResponseDto;
import com.vacancy.service.UserService;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final VacancyService vacancyService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        Page<User> userPage = userService.getAllUsers(page, size);
        
        List<UserDto> dtos = userPage.getContent().stream().map(UserDto::new).toList();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(userPage.getTotalElements()));
        
        return ResponseEntity.ok().headers(headers).body(dtos);
    }


    // ------------------------------ CRUD for users ------------------------------


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new UserDto(user));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        User savedUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDto(savedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        User updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(new UserDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    // ------------------------------ favorites ------------------------------


    @GetMapping("/{id}/favorites")
    public ResponseEntity<List<Vacancy>> getUserFavorites(@PathVariable Long id) {
        List<Vacancy> favorites = userService.getUserFavorites(id);
        return ResponseEntity.ok(favorites);
    }

    @PutMapping("/{userId}/favorite/{vacancyId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable Long vacancyId, @PathVariable Long userId) {
        vacancyService.addToFavorites(vacancyId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/favorite/{vacancyId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long vacancyId, @PathVariable Long userId) {
        vacancyService.removeFromFavorites(vacancyId, userId);
        return ResponseEntity.ok().build();
    }


    // ------------------------------ responses ------------------------------


    @GetMapping("/{userId}/responses")
    public ResponseEntity<List<UserVacancyResponseDto>> getUserResponses(@PathVariable Long userId) {
        List<UserVacancyResponseDto> responses = userService.getUserResponses(userId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{userId}/respond/{vacancyId}")
    public ResponseEntity<Void> respondToVacancy(@PathVariable Long vacancyId, @PathVariable Long userId) {
        vacancyService.respondToVacancy(vacancyId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/respond/{vacancyId}")
    public ResponseEntity<Void> removeResponseFromVacancy(@PathVariable Long vacancyId, @PathVariable Long userId) {
        vacancyService.removeResponseFromVacancy(vacancyId, userId);
        return ResponseEntity.ok().build();
    }

}
