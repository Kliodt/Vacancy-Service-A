package com.vacancy.model.dto;


import com.vacancy.model.entities.Organization;
import com.vacancy.model.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationDto {

    private Long id;

    @NotBlank(message = "Nickname не может быть пустым")
    @Size(max = 50, message = "Nickname не может превышать 50 символов")
    private String nickname;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен иметь правильный формат")
    @Size(max = 100, message = "Email не может превышать 100 символов")
    private String email;


    public OrganizationDto(Organization org) {
        this.id = org.getId();
        this.nickname = org.getNickname();
        this.email = org.getEmail();
    }

    public Organization createOrganization() {
        Organization org = new Organization(nickname, email);
        updateOrganization(org);
        return org;
    }

    public void updateOrganization(Organization user) {
        user.setId(this.id);
        user.setNickname(this.nickname);
        user.setEmail(this.email);
    }
}
