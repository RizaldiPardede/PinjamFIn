package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import com.pinjemFin.PinjemFin.repository.CustomerRepository;
import com.pinjemFin.PinjemFin.repository.UsersRepository;
import com.pinjemFin.PinjemFin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class CekUpdateAkunService {
    @Autowired
    private CustomerRepository CustomerRepository;


    @Transactional
    public ResponseEntity<?> cekUpdateAkun(UUID id_user) {
        Optional<UsersCustomer> usersCustomerOptional = CustomerRepository.findByUsersIdUser(id_user);
        if (usersCustomerOptional.isPresent()) {
            return ResponseEntity.ok(usersCustomerOptional.get());
        }
        else {
            return ResponseEntity
                    .status(404) // HTTP 404 Not Found
                    .body("{\"response\":\"Silakan update akun terlebih dahulu\"}");
        }

    }
}
