package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.UserEmployeUsersRequest;
import com.pinjemFin.PinjemFin.models.Role;
import com.pinjemFin.PinjemFin.models.UsersEmployee;
import com.pinjemFin.PinjemFin.repository.RoleRepository;
import com.pinjemFin.PinjemFin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    RoleService roleService;

    @GetMapping("/getallRoles")
    public List<Role> getallRoles() {
        return  roleService.getAllRoles();
    }

    @PostMapping("/create")
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
    }


}
