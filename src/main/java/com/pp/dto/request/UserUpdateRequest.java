package com.pp.dto.request;


import com.pp.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

    @Size(max=30)
    @NotBlank(message="Please provide your name")
    private String name;

    @Size(max=30)
    @NotBlank(message="Please provide your job")
    private String job;

    @Size(max=100)
    @NotBlank(message="Please provide your location")
    private String location;

    @Email(message="Please provide your email")
    private String email;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Please provide valid phone number")
    @Size(max=14)
    @NotBlank(message="Please provide your phone number")
    private String phone;


    @Size(max=15)
    @NotBlank(message="Please provide your website")
    private String website;


    private String aboutMe;





}
