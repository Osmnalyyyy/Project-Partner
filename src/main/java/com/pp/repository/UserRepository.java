package com.pp.repository;


import com.pp.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Boolean existsByEmail(String email);
	@EntityGraph(attributePaths = {"roles","projectsCreated","projectsFollowed","projectsParticipated"})
	Optional<User> findByEmail(String email);
	

	@EntityGraph(attributePaths = "roles")
	List<User> findAll();
	
	
	@EntityGraph(attributePaths = "roles")
	Optional<User> findById(Long id);

	@EntityGraph(attributePaths = "id")
	Optional<User> findUserById(Long id);
	
	@EntityGraph(attributePaths = "roles")
	Page<User> findAll(Pageable pageable);
	
	
	@Query("UPDATE User u SET u.name=:name,u.job=:job,u.location=:location,u.email=:email,u.phone=:phone,u.website=:website,u.aboutMe=:aboutMe WHERE u.id=:id")
	@Modifying
	void update(@Param("id")Long id,
				@Param("name")String name,
				@Param("job")String job,
				@Param("location")String location,
				@Param("email")String email,
				@Param("phone")String phone,
				@Param("website")String website,
				@Param("aboutMe")String aboutMe);
	
	
}
