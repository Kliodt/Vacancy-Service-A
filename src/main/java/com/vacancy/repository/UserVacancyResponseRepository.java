package com.vacancy.repository;

import com.vacancy.model.entities.UserVacancyResponse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserVacancyResponseRepository extends CrudRepository<UserVacancyResponse, Long> {
    
    List<UserVacancyResponse> findByUserId(Long userId);
    
    List<UserVacancyResponse> findByVacancyId(Long vacancyId);
    
    Optional<UserVacancyResponse> findByUserIdAndVacancyId(Long userId, Long vacancyId);
    
    void deleteByUserIdAndVacancyId(Long userId, Long vacancyId);
    
    boolean existsByUserIdAndVacancyId(Long userId, Long vacancyId);
}