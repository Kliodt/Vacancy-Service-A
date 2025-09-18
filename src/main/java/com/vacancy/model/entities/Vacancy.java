package com.vacancy.model.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Data
@NoArgsConstructor
@Entity
@Table
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "Описание вакансии не может быть пустым")
    @Size(max = 255, message = "Описание не может превышать 255 символов")
    private @NotNull String description;

    @Column(nullable = false, columnDefinition = "text")
    @NotBlank(message = "Подробное описание вакансии не может быть пустым")
    private @NotNull String longDescription;

    @Size(max = 100, message = "Название отдела не может превышать 100 символов")
    private @Nullable String department;

    @Min(value = 0, message = "Минимальная зарплата не может быть отрицательной")
    private @Nullable Integer minSalary;

    @Min(value = 0, message = "Максимальная зарплата не может быть отрицательной")
    private @Nullable Integer maxSalary;

    @Size(max = 100, message = "Название города не может превышать 100 символов")
    private @Nullable String city;

}
