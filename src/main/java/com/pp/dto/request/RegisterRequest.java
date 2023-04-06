package com.pp.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	@Size(max = 50)
	@NotBlank(message = "Please provide your name")
	private String name;

	@Email(message = "Please provide your email")
	@Size(min = 5, max = 50)
	private String email;

	@Size(min = 4, max = 20, message = "Please provide Correct Size of Password")
	@NotBlank(message = "Please provide your password")
	private String password;
}
