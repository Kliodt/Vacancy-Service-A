package com.vacancy;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class VacancyApplication {

    public static void main(String[] args) {
        SpringApplication.run(VacancyApplication.class, args);
    }

    // todo: @JsonNaming аннотации - выбрать snake case (можно в application.yml)
    // todo: Исправить костыли с id
    // todo: Сделать чтобы сервис не зависел от нескольких репозиториев
    // todo: Возможно разделить dto на in, out - частая практика

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info().title("Vacancy app API").version("1.0.0"));
    }
}
