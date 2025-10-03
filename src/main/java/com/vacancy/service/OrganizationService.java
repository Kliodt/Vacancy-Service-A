package com.vacancy.service;

import com.vacancy.model.dto.OrganizationDto;
import com.vacancy.model.dto.VacancyDto;
import com.vacancy.model.entities.Organization;
import com.vacancy.model.entities.Vacancy;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrganizationService {
    Page<Organization> getAllOrganizations(int page, int size);
    Organization getOrganizationById(Long id);
    Organization createOrganization(OrganizationDto organizationDto);
    Organization updateOrganization(Long id, OrganizationDto organizationDto);
    void deleteOrganization(Long id);
    List<Vacancy> getOrganizationVacancies(Long id);
    Vacancy publishVacancy(Long organizationId, VacancyDto vacancyDto);
    Vacancy updateOrganizationVacancy(Long organizationId, Long vacancyId, VacancyDto vacancyDto);
    void deleteOrganizationVacancy(Long organizationId, Long vacancyId);
}
