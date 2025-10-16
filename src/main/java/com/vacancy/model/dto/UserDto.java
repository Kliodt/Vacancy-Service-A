package com.vacancy.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vacancy.model.entities.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {
    private Long id;

    @NotBlank(message = "Nickname не может быть пустым")
    @Size(max = 50, message = "Nickname не может превышать 50 символов")
    private String nickname;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен иметь правильный формат")
    @Size(max = 100, message = "Email не может превышать 100 символов")
    private String email;

    @Size(max = 512, message = "CV Link не может превышать 512 символов")
    private String cvLink;

    public UserDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.cvLink = user.getCvLink();
    }

    public User createUser() {
        User u = new User(nickname, email);
        updateUser(u);
        return u;
    }

    public void updateUser(User user) {
        user.setId(this.id);
        user.setNickname(this.nickname);
        user.setEmail(this.email);
        user.setCvLink(this.cvLink);
    }
}
