package com.pp.repository;

import com.pp.domain.Role;
import com.pp.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

	
	Optional<Role> findByRoleName(RoleType roleName);
	
	
}
