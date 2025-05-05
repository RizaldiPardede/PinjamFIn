package com.pinjemFin.PinjemFin.config;

import com.pinjemFin.PinjemFin.models.Role;
import com.pinjemFin.PinjemFin.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class StartupConfig {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            List<String> roles = List.of("back office", "branch manager", "customer", "marketing", "super admin");

            for (String roleName : roles) {
                Role existingRole = roleRepository.findByNamaRole(roleName);
                if (existingRole == null) {
                    Role role = new Role();
                    role.setNama_role(roleName);
                    roleRepository.save(role);
                    System.out.println("Inserted role: " + roleName);
                } else {
                    System.out.println("Role already exists: " + roleName);
                }
            }
        };
    }

}
