package com.vacancy.controllers;

import com.vacancy.model.entities.User;
import com.vacancy.model.entities.Vacancy;
import com.vacancy.exceptions.RequestException;
import com.vacancy.model.dto.UserDto;
import com.vacancy.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> dtos = ((List<User>)userRepository.findAll()).stream().map(UserDto::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }   
        return ResponseEntity.ok(new UserDto(user.get()));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        if (userRepository.findUserByEmail(userDto.getEmail()) != null) {
            throw new RequestException(HttpStatus.CONFLICT, "Пользователь с таким email уже зарегистрирован");
        }
        User user = userDto.toUser();
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDto(savedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        if (!userRepository.existsById(id)) {
            throw new RequestException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        User existing = userRepository.findUserByEmail(userDto.getEmail());
        if (existing != null && !id.equals(existing.getId())) {
            throw new RequestException(HttpStatus.CONFLICT, "С таким email уже зарегистрирован другой пользователь");
        }
        User user = userDto.toUser();
        user.setId(id);
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(new UserDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/favorites")
    public ResponseEntity<List<Vacancy>> getUserFavorites(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return ResponseEntity.ok(user.get().getFavoriteList());
    }

    @GetMapping("/{id}/responses")
    public ResponseEntity<List<Vacancy>> getUserResponses(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return ResponseEntity.ok(user.get().getResponseList());
    }
}
