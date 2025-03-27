package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.UserEmployeUsersRequest;
import com.pinjemFin.PinjemFin.models.UsersEmployee;
import com.pinjemFin.PinjemFin.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/createAkunEmplooyee")
    public ResponseEntity<UsersEmployee> createCustomer(@RequestBody UserEmployeUsersRequest usersEmployee) {

        UsersEmployee savedEmployee = employeeService.addEmployee(usersEmployee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }
}
