package com.vacancy.controllers;

import com.vacancy.model.entities.User;
import com.vacancy.model.entities.Vacancy;
import com.vacancy.model.dto.UserDto;
import com.vacancy.service.UserService;
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

    @GetMapping("/{id}/favorites")
    public ResponseEntity<List<Vacancy>> getUserFavorites(@PathVariable Long id) {
        List<Vacancy> favorites = userService.getUserFavorites(id);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/{id}/responses")
    public ResponseEntity<List<Vacancy>> getUserResponses(@PathVariable Long id) {
        List<Vacancy> responses = userService.getUserResponses(id);
        return ResponseEntity.ok(responses);
    }
}
