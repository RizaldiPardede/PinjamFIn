package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.models.Role;
import com.pinjemFin.PinjemFin.repository.RoleFeatureRepository;
import com.pinjemFin.PinjemFin.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private RoleFeatureRepository roleFeatureRepository;


    public List<Role> getAllRoles() {

        return roleRepository.findAll();
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role tidak ditemukan"));

        // Pengecekan hardcode untuk role yang tidak boleh dihapus
        if (role.getNama_role().equalsIgnoreCase("back office") ||
                role.getNama_role().equalsIgnoreCase("branch manager") ||
                role.getNama_role().equalsIgnoreCase("customer") ||
                role.getNama_role().equalsIgnoreCase("marketing") ||
                role.getNama_role().equalsIgnoreCase("super admin")
                || role.getNama_role().equalsIgnoreCase("Rizaldi")
        ) {

            System.out.println("Role default tidak dapat dihapus: " + role.getNama_role());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Role default tidak dapat dihapus: " + role.getNama_role());
        }
        else{
            roleFeatureRepository.deleteByIdRole(role.getId_role());
            roleRepository.delete(role);
        }

    }



}
