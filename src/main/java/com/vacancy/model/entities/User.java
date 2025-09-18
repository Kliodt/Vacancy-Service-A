package com.vacancy.model.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
@Table(name = "app_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Nickname не может быть пустым")
    @Size(max = 50, message = "Nickname не может превышать 50 символов")
    private @NotNull String nickname;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен иметь правильный формат")
    @Size(max = 100, message = "Email не может превышать 100 символов")
    private @NotNull String email;

    @Column(nullable = false)
    @NotBlank(message = "Password hash не может быть пустым")
    private @NotNull String passwordHash;


    @Column(nullable = false, length = 512)
    @Size(max = 512, message = "CV Link не может превышать 512 символов")
    private @Nullable String cvLink;

    @ManyToMany(fetch = FetchType.LAZY)
    private @NotNull List<Vacancy> favoriteList = new ArrayList<>(); // избранное

    @ManyToMany(fetch = FetchType.LAZY)
    private @NotNull List<Vacancy> responseList = new ArrayList<>(); // отклики
}
