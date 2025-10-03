package com.vacancy.service;

import com.vacancy.exceptions.RequestException;
import com.vacancy.model.dto.OrganizationDto;
import com.vacancy.model.dto.VacancyDto;
import com.vacancy.model.entities.Organization;
import com.vacancy.model.entities.Vacancy;
import com.vacancy.repository.OrganizationRepository;
import com.vacancy.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final VacancyRepository vacancyRepository;

    public Page<Organization> getAllOrganizations(int page, int size) {
        if (size > 50) {
            size = 50;
        }
        Pageable pageable = PageRequest.of(page, size);
        return organizationRepository.findAll(pageable);
    }

    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new RequestException(HttpStatus.NOT_FOUND, "Организация не найдена"));
    }

    @Transactional
    public Organization createOrganization(OrganizationDto organizationDto) {
        if (organizationRepository.findOrganizationByEmail(organizationDto.getEmail()) != null) {
            throw new RequestException(HttpStatus.CONFLICT, "Организация с таким email уже зарегистрирована");
        }
        organizationDto.setId(null);
        Organization organization = organizationDto.createOrganization();
        return organizationRepository.save(organization);
    }

    @Transactional
    public Organization updateOrganization(Long id, OrganizationDto organizationDto) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RequestException(HttpStatus.NOT_FOUND, "Организация не найдена"));
        
        Organization existing = organizationRepository.findOrganizationByEmail(organizationDto.getEmail());
        if (existing != null) {
            throw new RequestException(HttpStatus.CONFLICT, "С таким email уже зарегистрирована другая организация");
        }
        organizationDto.setId(id);        
        organizationDto.updateOrganization(organization);
        return organization;
    }

    @Transactional
    public void deleteOrganization(Long id) {
        organizationRepository.deleteById(id);
    }

    @Transactional
    public List<Vacancy> getOrganizationVacancies(Long id) {
        List<Vacancy> ret = organizationRepository.findById(id)
                .orElseThrow(() -> new RequestException(HttpStatus.NOT_FOUND, "Организация не найдена"))
                .getPublishedVacancies();
        Hibernate.initialize(ret);
        return ret;
    }

    @Transactional
    public Vacancy publishVacancy(Long organizationId, VacancyDto vacancyDto) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RequestException(HttpStatus.NOT_FOUND, "Организация не найдена"));
        vacancyDto.setId(null);
        Vacancy vacancy = vacancyDto.createVacancy();
        vacancy.setOrganization(organization);
        return vacancyRepository.save(vacancy);
    }

    @Transactional
    public Vacancy updateOrganizationVacancy(Long organizationId, Long vacancyId, VacancyDto vacancyDto) {        
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new RequestException(HttpStatus.NOT_FOUND, "Вакансия не найдена"));
        
        if (vacancy.getOrganization() == null || vacancy.getOrganization().getId() != organizationId) {
            throw new RequestException(HttpStatus.FORBIDDEN, "Вакансия не принадлежит данной организации");
        }
        vacancyDto.setId(vacancyId);
        vacancyDto.updateVacancy(vacancy);
        return vacancy;
    }

    @Transactional
    public void deleteOrganizationVacancy(Long organizationId, Long vacancyId) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new RequestException(HttpStatus.NOT_FOUND, "Вакансия не найдена"));
        
        if (vacancy.getOrganization() == null || vacancy.getOrganization().getId() != organizationId) {
            throw new RequestException(HttpStatus.FORBIDDEN, "Вакансия не принадлежит данной организации");
        }
        
        vacancyRepository.delete(vacancy);
    }
}