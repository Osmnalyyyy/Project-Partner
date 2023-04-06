package com.pp.config;

import com.pp.domain.Role;
import com.pp.domain.enums.RoleType;
import com.pp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public void run(String... args) throws Exception {


   roleRepository.save(new Role( 1,RoleType.ROLE_USER));
    roleRepository.save(new Role(2,RoleType.ROLE_ADMIN));
    }
}

