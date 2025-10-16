package com.vacancy.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vacancy.model.entities.Vacancy;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;


// todo: мапперы для дто

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VacancyDto {

    private Long id;

    @NotBlank(message = "Описание вакансии не может быть пустым")
    @Size(max = 255, message = "Описание не может превышать 255 символов")
    private String description;

    @NotBlank(message = "Подробное описание вакансии не может быть пустым")
    private String longDescription;

    @Min(value = 0, message = "Минимальная зарплата не может быть отрицательной")
    private Integer minSalary;

    @Min(value = 0, message = "Максимальная зарплата не может быть отрицательной")
    private Integer maxSalary;

    @Size(max = 100, message = "Название города не может превышать 100 символов")
    private String city;

    private Long organizationId;

    public VacancyDto(Vacancy vacancy) {
        this.id = vacancy.getId();
        this.description = vacancy.getDescription();
        this.longDescription = vacancy.getLongDescription();
        this.minSalary = vacancy.getMinSalary();
        this.maxSalary = vacancy.getMaxSalary();
        this.city = vacancy.getCity();
        
        if (vacancy.getOrganization() != null) {
            this.organizationId = vacancy.getOrganization().getId();
        }
    }

    public Vacancy createVacancy() {
        Vacancy v = new Vacancy(description, longDescription);
        updateVacancy(v);
        return v;
    }

    public void updateVacancy(Vacancy vacancy) {
        vacancy.setId(this.id);
        vacancy.setDescription(this.description);
        vacancy.setLongDescription(this.longDescription);
        vacancy.setMinSalary(this.minSalary);
        vacancy.setMaxSalary(this.maxSalary);
        vacancy.setCity(this.city);
    }

    @AssertTrue(message = "Минимальная зарплата не может быть больше максимальной")
    boolean isSalaryRangeValid() {
        return this.minSalary != null && this.maxSalary != null && this.minSalary <= this.maxSalary;
    }
}
