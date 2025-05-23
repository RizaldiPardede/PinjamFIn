package com.pinjemFin.PinjemFin.config;

import com.pinjemFin.PinjemFin.models.Plafon;
import com.pinjemFin.PinjemFin.models.Role;
import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.models.UsersEmployee;
import com.pinjemFin.PinjemFin.repository.EmployeeRepository;
import com.pinjemFin.PinjemFin.repository.PlafonRepository;
import com.pinjemFin.PinjemFin.repository.RoleRepository;
import com.pinjemFin.PinjemFin.repository.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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

    @Bean
    CommandLineRunner initSuperAdmin(UsersRepository usersRepository,
                                     RoleRepository roleRepository,
                                     EmployeeRepository employeeRepository) {
        return args -> {
            String email = "superadmin@pinjemfin.com";
            Optional<Users> existing = usersRepository.findByEmail(email);

            if (existing.isEmpty()) {
                // Buat akun super admin
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String rawPassword = UUID.randomUUID().toString().substring(0, 8);
                String hashedPassword = encoder.encode(rawPassword);

                Role superAdminRole = roleRepository.findByNamaRole("super admin");

                Users superAdmin = new Users();
                superAdmin.setEmail(email);
                superAdmin.setPassword(hashedPassword);
                superAdmin.setNama("Super Admin");
                superAdmin.setIsActive(true);
                superAdmin.setRole(superAdminRole);

                Users saved = usersRepository.save(superAdmin);

                // Jika super admin juga butuh UsersEmployee:
                UsersEmployee employee = new UsersEmployee();
                employee.setNip(999999); // Atur NIP default
                employee.setJabatan("super admin");
                employee.setUsers(saved);
                employee.setBranch(null); // Atau isi jika ada branch default

                employeeRepository.save(employee);

                System.out.println("✅ Super Admin berhasil dibuat:");
                System.out.println("Email: " + email);
                System.out.println("Password: " + rawPassword);
            } else {
                System.out.println("Super Admin sudah ada: " + email);
            }
        };
    }


}
