package com.pp.domain.enums;

public enum RoleType {

	ROLE_USER("User"),
	ROLE_ADMIN("Administrator");
	
	private final String name;
	private RoleType(String name) {
		this.name=name;
	}

	public String getName() {
		return name;
	}

	
	
	
	
}
