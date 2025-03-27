package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.dto.UserEmployeUsersRequest;
import com.pinjemFin.PinjemFin.models.Branch;
import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.models.UsersEmployee;
import com.pinjemFin.PinjemFin.repository.BranchRepository;
import com.pinjemFin.PinjemFin.repository.EmployeeRepository;
import com.pinjemFin.PinjemFin.repository.RoleRepository;
import com.pinjemFin.PinjemFin.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BranchRepository branchRepository;

    public UsersEmployee addEmployee(UserEmployeUsersRequest usersEmployeeUsersRequest) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Users users = new Users();
        users.setEmail(usersEmployeeUsersRequest.getUsers().getEmail());
        users.setPassword(passwordEncoder.encode(usersEmployeeUsersRequest.getUsers().getPassword())); //hasing password
        users.setNama(usersEmployeeUsersRequest.getUsers().getNama());
        users.setRole(roleRepository.findById(usersEmployeeUsersRequest.getUsers().getId_role()).get());
        Users usersaved =usersRepository.save(users);

        UsersEmployee usersEmployee = new UsersEmployee();
        usersEmployee.setNip(usersEmployeeUsersRequest.getNip());
        usersEmployee.setJabatan(usersEmployeeUsersRequest.getJabatan());
        usersEmployee.setUsers(usersaved);
        usersEmployee.setBranch(branchRepository.findById(usersEmployeeUsersRequest.getIdbranch()).get());

        return employeeRepository.save(usersEmployee);
    }

}
