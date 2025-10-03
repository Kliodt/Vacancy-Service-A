package com.vacancy.service;

import com.vacancy.model.dto.UserDto;
import com.vacancy.model.dto.UserVacancyResponseDto;
import com.vacancy.model.entities.User;
import com.vacancy.model.entities.Vacancy;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    Page<User> getAllUsers(int page, int size);
    User getUserById(Long id);
    User createUser(UserDto userDto);
    User updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    List<Vacancy> getUserFavorites(Long id);
    List<UserVacancyResponseDto> getUserResponses(Long id);
}
