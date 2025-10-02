package com.vacancy.repository;


import com.vacancy.model.entities.Vacancy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface VacancyRepository extends CrudRepository<Vacancy, Long>, PagingAndSortingRepository<Vacancy, Long> {
}
