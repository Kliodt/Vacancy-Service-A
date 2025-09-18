package com.vacancy.model.dto;

import com.vacancy.model.entities.Vacancy;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VacancyDto {

    private Long id;

    @NotBlank(message = "Описание вакансии не может быть пустым")
    @Size(max = 255, message = "Описание не может превышать 255 символов")
    private String description;

    @NotBlank(message = "Подробное описание вакансии не может быть пустым")
    private String longDescription;

    @Size(max = 100, message = "Название отдела не может превышать 100 символов")
    private String department;

    @Min(value = 0, message = "Минимальная зарплата не может быть отрицательной")
    private Integer minSalary;

    @Min(value = 0, message = "Максимальная зарплата не может быть отрицательной")
    private Integer maxSalary;

    @Size(max = 100, message = "Название города не может превышать 100 символов")
    private String city;

    public VacancyDto(Vacancy vacancy) {
        this.id = vacancy.getId();
        this.description = vacancy.getDescription();
        this.longDescription = vacancy.getLongDescription();
        this.department = vacancy.getDepartment();
        this.minSalary = vacancy.getMinSalary();
        this.maxSalary = vacancy.getMaxSalary();
        this.city = vacancy.getCity();
    }

    public Vacancy toVacancy() {
        Vacancy vacancy = new Vacancy();
        vacancy.setId(this.id);
        vacancy.setDescription(this.description);
        vacancy.setLongDescription(this.longDescription);
        vacancy.setDepartment(this.department);
        vacancy.setMinSalary(this.minSalary);
        vacancy.setMaxSalary(this.maxSalary);
        vacancy.setCity(this.city);
        return vacancy;
    }

    @AssertTrue(message = "Минимальная зарплата не может быть больше максимальной")
    boolean isSalaryRangeValid() {
        return this.minSalary != null && this.maxSalary != null && this.minSalary <= this.maxSalary;
    }
}
