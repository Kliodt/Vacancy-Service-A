package com.vacancy.model.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    public enum Role {
        USER, ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false) // todo: length
    private @NotNull String nickname;

    @Column(nullable = false) // todo: length
    private @NotNull String email;

    @Column(nullable = false)
    private @NotNull String passwordHash;

    @Column(nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private @NotNull Role role;

    @Column(nullable = false, length = 512)
    private @Nullable String cvLink;

    @ManyToMany(fetch = FetchType.LAZY)
    private @NotNull List<Vacancy> favoriteList = new ArrayList<>(); // избранное

    @ManyToMany(fetch = FetchType.LAZY)
    private @NotNull List<Vacancy> responseList = new ArrayList<>(); // отклики
}
