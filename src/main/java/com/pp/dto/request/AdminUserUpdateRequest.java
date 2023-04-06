package com.pp.dto.request;

import com.pp.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserUpdateRequest {
    @Size(max=30)
    @NotBlank(message="Please provide your name")
    private String name;

    @Size(max=30)
    @NotBlank(message="Please provide your job")
    private String job;

    @Size(max=100)
    @NotBlank(message="Please provide your address")
    private String location;

    @Email(message="Please provide your email")
    private String email;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Please provide valid phone number")
    @Size(max=14)
    @NotBlank(message="Please provide your phone number")
    private String phone;

    @Size(min=4,max=20,message="Please provide Correct Size of Password")
    @NotBlank(message="Please provide your First name")
    private String password;

    @Size(max=15)
    @NotBlank(message="Please provide your zip-code")
    private String website;

    private Boolean builtIn;

    private Set<String> roles;
}
