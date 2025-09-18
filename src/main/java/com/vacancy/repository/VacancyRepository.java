package com.vacancy.repository;


import com.vacancy.model.entities.Vacancy;
import org.springframework.data.repository.CrudRepository;


public interface VacancyRepository extends CrudRepository<Vacancy, Long> {
}
