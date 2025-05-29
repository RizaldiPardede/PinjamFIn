package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.UserEmployeUsersRequest;
import com.pinjemFin.PinjemFin.models.Role;
import com.pinjemFin.PinjemFin.models.UsersEmployee;
import com.pinjemFin.PinjemFin.repository.RoleRepository;
import com.pinjemFin.PinjemFin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    RoleService roleService;

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_getAllRole')")
    @GetMapping("/getallRoles")
    public List<Role> getallRoles() {
        return  roleService.getAllRoles();
    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_createRole')")
    @PostMapping("/create")
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_deleteRole')")
    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
    }


}
