package com.pp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="t_user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 50,nullable = false)
	private String name;

	@Column(length = 50)
	private String job;

	@Column(length = 100)
	private String location;
	
	@Column(length = 80,nullable = false,unique = true)
	private String email;

	@Column(length = 14)
	private String phone;

	@Column(length = 120,nullable = false)	
	private String password;

	@Column(length = 15)
	private String website;

	@Column
	private String aboutMe;
	
	@Column(nullable = false)
	private Boolean builtIn=false;

	@OneToMany(mappedBy = "user")
	private Set<Participant> userParticipantDetails = new HashSet<>();

	@ManyToMany
	@JoinTable( name="t_user_role",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="role_id"))
	private  Set<Role> roles = new HashSet<>();

	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
	private Set<Project> projectsCreated = new HashSet<>();

	@ManyToMany
	@JoinTable(
			name = "t_user_followed_projects",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "project_id"))
	private Set<Project> projectsFollowed = new HashSet<>();

	@ManyToMany
	@JoinTable(
			name = "t_user_participated_projects",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "project_id"))
	private Set<Project> projectsParticipated = new HashSet<>();

}
