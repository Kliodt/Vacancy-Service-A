package com.vacancy.service;

import com.vacancy.model.dto.UserVacancyResponseDto;
import com.vacancy.repository.UserVacancyResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserVacancyResponseService {

    private final UserVacancyResponseRepository responseRepository;

    public List<UserVacancyResponseDto> getUserResponses(Long userId) {
        return responseRepository.findByUserId(userId)
                .stream()
                .map(UserVacancyResponseDto::new)
                .toList();
    }

    public List<UserVacancyResponseDto> getVacancyResponses(Long vacancyId) {
        return responseRepository.findByVacancyId(vacancyId)
                .stream()
                .map(UserVacancyResponseDto::new)
                .toList();
    }
}