package com.vacancy.service;

import com.vacancy.exceptions.RequestException;
import com.vacancy.model.dto.UserDto;
import com.vacancy.model.dto.UserVacancyResponseDto;
import com.vacancy.model.entities.User;
import com.vacancy.model.entities.Vacancy;
import com.vacancy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserVacancyResponseService responseService;

    public Page<User> getAllUsers(int page, int size) {
        if (size > 50) {
            size = 50;
        }
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RequestException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

    public User createUser(UserDto userDto) {
        if (userRepository.findUserByEmail(userDto.getEmail()) != null) {
            throw new RequestException(HttpStatus.CONFLICT, "Пользователь с таким email уже зарегистрирован");
        }
        User user = userDto.createUser();
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RequestException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
        userDto.updateUser(user);
        return user;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<Vacancy> getUserFavorites(Long id) {
        User user = getUserById(id);
        return user.getFavoriteList();
    }

    public List<UserVacancyResponseDto> getUserResponses(Long id) {
        getUserById(id);
        return responseService.getUserResponses(id);
    }
}