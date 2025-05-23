package com.pinjemFin.PinjemFin.config;

import com.pinjemFin.PinjemFin.models.Plafon;
import com.pinjemFin.PinjemFin.models.Role;
import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.models.UsersEmployee;
import com.pinjemFin.PinjemFin.repository.EmployeeRepository;
import com.pinjemFin.PinjemFin.repository.PlafonRepository;
import com.pinjemFin.PinjemFin.repository.RoleRepository;
import com.pinjemFin.PinjemFin.repository.UsersRepository;
import com.pinjemFin.PinjemFin.service.EmailService;
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
                                     EmployeeRepository employeeRepository,
                                     EmailService emailService) {
        return args -> {
            String email = "superadmin@pinjemfin.com";
            if (usersRepository.findByEmail(email).isEmpty()) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String rawPassword = UUID.randomUUID().toString().substring(0, 8);
                String hashedPassword = encoder.encode(rawPassword);

                Role superAdminRole = roleRepository.findByNamaRole("super admin");

                Users user = new Users();
                user.setEmail(email);
                user.setPassword(hashedPassword);
                user.setNama("Super Admin");
                user.setIsActive(true);
                user.setRole(superAdminRole);
                Users savedUser = usersRepository.save(user);

                UsersEmployee employee = new UsersEmployee();
                employee.setNip(999999); // NIP unik sementara
                employee.setJabatan("super admin");
                employee.setUsers(savedUser);
                employee.setBranch(null); // set sesuai kebutuhan kalau pakai branch
                employeeRepository.save(employee);

                // ✅ Kirim email akun
                String subject = "Akun Super Admin Berhasil Dibuat";
                String body = "Email: " + email + "\n" +
                        "Password: " + rawPassword + "\n\n" +
                        "Silakan login dan segera ubah password Anda.";
                emailService.sendEmail(email, subject, body);

                System.out.println("Super Admin berhasil dibuat dan email dikirim.");
            } else {
                System.out.println("Super Admin sudah ada.");
            }
        };
    }


}
