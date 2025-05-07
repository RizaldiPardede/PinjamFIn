package com.pinjemFin.PinjemFin.config;

import com.pinjemFin.PinjemFin.models.Plafon;
import com.pinjemFin.PinjemFin.models.Role;
import com.pinjemFin.PinjemFin.repository.PlafonRepository;
import com.pinjemFin.PinjemFin.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


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

    @Bean
    CommandLineRunner initPlafon(PlafonRepository plafonRepository) {
        return args -> {
            if (plafonRepository.count() == 0) {
                Plafon bronze = new Plafon();
                bronze.setJenis_plafon("Bronze");
                bronze.setJumlah_plafon(1_000_000.0);
                bronze.setBunga(5.0);

                Plafon gold = new Plafon();
                gold.setJenis_plafon("Gold");
                gold.setJumlah_plafon(25_000_000.0);
                gold.setBunga(3.0);

                Plafon platinum = new Plafon();
                platinum.setJenis_plafon("Platinum");
                platinum.setJumlah_plafon(100_000_000.0);
                platinum.setBunga(2.0);

                Plafon silver = new Plafon();
                silver.setJenis_plafon("Silver");
                silver.setJumlah_plafon(5_000_000.0);
                silver.setBunga(4.5);

                plafonRepository.saveAll(List.of(bronze, gold, platinum, silver));
                System.out.println("Inserted default plafon data.");
            } else {
                System.out.println("Plafon data already exists.");
            }
        };
    }


}
