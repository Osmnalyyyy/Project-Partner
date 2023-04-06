package com.pp.service;

import com.pp.domain.Role;
import com.pp.domain.enums.RoleType;
import com.pp.exception.ResourceNotFoundException;
import com.pp.exception.message.ErrorMessage;
import com.pp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	public Role findByType(RoleType roleType) {
		Role role = roleRepository.findByRoleName(roleType).orElseThrow(() -> new ResourceNotFoundException(
				String.format(ErrorMessage.ROLE_NOT_FOUND_MESSAGE, roleType.name())));
		return role;
	}

}
