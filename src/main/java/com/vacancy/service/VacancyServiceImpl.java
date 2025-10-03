package com.vacancy.service;

import com.vacancy.exceptions.RequestException;
import com.vacancy.model.entities.User;
import com.vacancy.model.entities.UserVacancyResponse;
import com.vacancy.model.entities.Vacancy;
import com.vacancy.repository.UserVacancyResponseRepository;
import com.vacancy.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;
    private final UserVacancyResponseRepository responseRepository;
    private final UserService userService;

    public Page<Vacancy> getAllVacancies(int page, int size) {
        if (size > 50) {
            size = 50;
        }
        Pageable pageable = PageRequest.of(page, size);
        return vacancyRepository.findAll(pageable);
    }

    public Vacancy getVacancyById(Long id) {
        return vacancyRepository.findById(id)
                .orElseThrow(() -> new RequestException(HttpStatus.NOT_FOUND, "Вакансия не найдена"));
    }

    @Transactional
    public void respondToVacancy(Long vacancyId, Long userId) {
        User user = userService.getUserById(userId);
        Vacancy vacancy = getVacancyById(vacancyId);
        
        if (!responseRepository.existsByUserIdAndVacancyId(userId, vacancyId)) {
            UserVacancyResponse response = new UserVacancyResponse(user, vacancy);
            responseRepository.save(response);
        }
    }

    @Transactional
    public void removeResponseFromVacancy(Long vacancyId, Long userId) {
        responseRepository.deleteByUserIdAndVacancyId(userId, vacancyId);
    }

    @Transactional
    public void addToFavorites(Long vacancyId, Long userId) {
        User user = userService.getUserById(userId);

        if (getVacancyByIdFromList(user.getFavoriteList(), vacancyId) == null) {
            Vacancy vacancy = getVacancyById(vacancyId);
            user.getFavoriteList().add(vacancy);
        }
    }

    @Transactional
    public void removeFromFavorites(Long vacancyId, Long userId) {
        User user = userService.getUserById(userId);
        user.getFavoriteList().removeIf(v -> v.getId() == vacancyId);
    }

    private Vacancy getVacancyByIdFromList(List<Vacancy> list, Long id) {
        for (Vacancy vacancy : list) {
            if (vacancy.getId() == id) return vacancy;
        }
        return null;
    }
}