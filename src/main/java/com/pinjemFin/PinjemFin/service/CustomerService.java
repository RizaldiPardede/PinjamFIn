package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.models.Plafon;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import com.pinjemFin.PinjemFin.repository.CustomerRepository;
import com.pinjemFin.PinjemFin.repository.PlafonRepository;
import com.pinjemFin.PinjemFin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository CustomerRepository;

    @Autowired
    private PlafonRepository plafonRepository;

    @Autowired
    PinjamanService pinjamanService;


    private final JwtUtil jwtUtil;

    @Autowired
    public CustomerService(JwtUtil jwtUtil) {  // Constructor Injection
        this.jwtUtil = jwtUtil;
    }



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

    public UsersCustomer addCustomer(UsersCustomer usersCustomer) {
        return CustomerRepository.save(usersCustomer);
    }



    @Transactional
    public UsersCustomer partialUpdate(UUID id, Map<String, Object> updates) {
        return CustomerRepository.findById(id).map(existingCustomer -> {
            updates.forEach((key, value) -> {
                Field field;
                try {
                    field = UsersCustomer.class.getDeclaredField(key);
                    field.setAccessible(true);
                    field.set(existingCustomer, value);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException("Error updating field: " + key);
                }
            });
            return CustomerRepository.save(existingCustomer);
        }).orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
    }

    public UUID getUserCustomerIdFromToken(String token) {
        // Ambil id_user langsung dari token JWT
        UUID idUser = UUID.fromString(jwtUtil.extractidUser(token));

        // Cari id_user_customer berdasarkan id_user
        return CustomerRepository.findByUsersIdUser(idUser)
                .map(UsersCustomer::getId_user_customer)
                .orElseThrow(() -> new RuntimeException("User Customer not found"));
    }

    public UsersCustomer getPlafon(String token) {
        List<Plafon> plafons = plafonRepository.findAllSorted();
        Double jumlPinjLunas = pinjamanService.getTotalPeminjamanLunasByUser("Bearer "+token);
        Plafon plafon = plafons.get(0);
        for (int i = 0; i < plafons.size(); i++){
            if(jumlPinjLunas>plafons.get(i).getJumlah_plafon()){
                plafon = plafons.get(i);
            }
            else{
                break;
            }
        }

        UsersCustomer usersCustomer = CustomerRepository.findById(getUserCustomerIdFromToken(token)).get();
        usersCustomer.setPlafon(plafon);


        return CustomerRepository.save(usersCustomer);
    }

    public UsersCustomer getUserCustomer(UUID id){
        return CustomerRepository.findById(id).get();
    }
}
