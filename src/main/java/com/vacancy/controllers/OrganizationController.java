package com.vacancy.controllers;

import com.vacancy.model.dto.OrganizationDto;
import com.vacancy.model.dto.UserVacancyResponseDto;
import com.vacancy.model.dto.VacancyDto;
import com.vacancy.model.entities.Organization;
import com.vacancy.model.entities.Vacancy;
import com.vacancy.service.OrganizationService;
import com.vacancy.service.UserVacancyResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;
    private final UserVacancyResponseService responseService;

    @GetMapping
    public ResponseEntity<List<OrganizationDto>> getAllOrganizations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        Page<Organization> organizationPage = organizationService.getAllOrganizations(page, size);
        
        List<OrganizationDto> dtos = organizationPage.getContent().stream()
                .map(OrganizationDto::new).toList();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(organizationPage.getTotalElements()));
        
        return ResponseEntity.ok().headers(headers).body(dtos);
    }


    // ------------------------------ CRUD for organizations ------------------------------


    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> getOrganizationById(@PathVariable Long id) {
        Organization organization = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(new OrganizationDto(organization));
    }

    @PostMapping
    public ResponseEntity<OrganizationDto> createOrganization(@Valid @RequestBody OrganizationDto organizationDto) {
        Organization savedOrganization = organizationService.createOrganization(organizationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OrganizationDto(savedOrganization));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDto> updateOrganization(@PathVariable Long id, @Valid @RequestBody OrganizationDto organizationDto) {
        Organization updatedOrganization = organizationService.updateOrganization(id, organizationDto);
        return ResponseEntity.ok(new OrganizationDto(updatedOrganization));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }


    // ------------------------------ Vacancy management ------------------------------


    @GetMapping("/{orgId}/vacancies")
    public ResponseEntity<List<VacancyDto>> getOrganizationVacancies(@PathVariable Long orgId) {
        List<Vacancy> vacancies = organizationService.getOrganizationVacancies(orgId);
        List<VacancyDto> dtos = vacancies.stream().map(VacancyDto::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{orgId}/vacancies")
    public ResponseEntity<VacancyDto> publishVacancy(
            @PathVariable Long orgId,
            @Valid @RequestBody VacancyDto vacancyDto) {
        Vacancy savedVacancy = organizationService.publishVacancy(orgId, vacancyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new VacancyDto(savedVacancy));
    }

    @PutMapping("/{orgId}/vacancies/{vacancyId}")
    public ResponseEntity<VacancyDto> updateOrganizationVacancy(
            @PathVariable Long orgId,
            @PathVariable Long vacancyId,
            @Valid @RequestBody VacancyDto vacancyDto) {
        Vacancy updatedVacancy = organizationService.updateOrganizationVacancy(orgId, vacancyId, vacancyDto);
        return ResponseEntity.ok(new VacancyDto(updatedVacancy));
    }

    @DeleteMapping("/{orgId}/vacancies/{vacancyId}")
    public ResponseEntity<Void> deleteOrganizationVacancy(
            @PathVariable Long orgId,
            @PathVariable Long vacancyId) {
        organizationService.deleteOrganizationVacancy(orgId, vacancyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orgId}/vacancies/{vacancyId}/responses")
    public ResponseEntity<List<UserVacancyResponseDto>> getVacancyResponses(
            @PathVariable Long orgId,
            @PathVariable Long vacancyId) {
        organizationService.getOrganizationById(orgId);
        List<UserVacancyResponseDto> responses = responseService.getVacancyResponses(vacancyId);
        return ResponseEntity.ok(responses);
    }
}
