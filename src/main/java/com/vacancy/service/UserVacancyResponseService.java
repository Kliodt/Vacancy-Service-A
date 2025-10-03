package com.vacancy.service;

import com.vacancy.model.dto.UserVacancyResponseDto;

import java.util.List;

public interface UserVacancyResponseService {
    List<UserVacancyResponseDto> getUserResponses(Long userId);
    List<UserVacancyResponseDto> getVacancyResponses(Long vacancyId);
}
