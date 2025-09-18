package com.vacancy.model.dto;


import com.vacancy.model.entities.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {

    public UserDto(User user) {

    }

    public User toUser() {

    }
}
