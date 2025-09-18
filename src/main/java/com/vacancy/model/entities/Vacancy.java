package com.vacancy.model.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


@Data
@NoArgsConstructor
@Entity
@Table
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private @NotNull String description;

    @Column(nullable = false, columnDefinition = "text")
    private @NotNull String longDescription;

    private @Nullable String department;

    private @Nullable Integer minSalary;

    private @Nullable Integer maxSalary;

    private @Nullable String city;

}
