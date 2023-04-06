package com.pp.dto;

import com.pp.domain.Project;
import com.pp.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	private Long id;
	private String name;
	private String job;
	private String location;
	private String email;
	private String phone;
	private String website;
	private String aboutMe;
	private Boolean builtIn;
	private Set<String> role;

	private Set<String> projectsCreated;

	private Set<String> projectsFollowed;

	private Set<String> projectsParticipated;

	public void setRoles(Set<Role> roles) {
		Set<String> roleString = new HashSet<>();
		roles.forEach(r -> {
			roleString.add(r.getRoleName().getName()); // Administrator veya Customer gözükecek
		});
		this.role = roleString;
	}

	public void setProjectsCreated(Set<Project> projects) {
		Set<String> projectString = new HashSet<>();
		projects.forEach(p -> {
			projectString.add(p.getId().toString());
		});
		this.projectsCreated = projectString;
	}

	public void setProjectsFollowed(Set<Project> projects) {
		Set<String> projectString = new HashSet<>();
		projects.forEach(p -> {
			projectString.add(p.getId().toString());
		});
		this.projectsFollowed = projectString;
	}

	public void setProjectsParticipated(Set<Project> projects) {
		Set<String> projectString = new HashSet<>();
		projects.forEach(p -> {
			projectString.add(p.getId().toString());
		});
		this.projectsParticipated = projectString;
	}

}
