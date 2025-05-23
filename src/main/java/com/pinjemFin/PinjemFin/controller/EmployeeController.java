package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.*;
import com.pinjemFin.PinjemFin.models.Pengajuan;
import com.pinjemFin.PinjemFin.models.UsersEmployee;
import com.pinjemFin.PinjemFin.models.pengajuan_userEmployee;
import com.pinjemFin.PinjemFin.service.EmployeeService;
import com.pinjemFin.PinjemFin.service.PengajuanEmployeeService;
import com.pinjemFin.PinjemFin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @Autowired
    PengajuanEmployeeService pengajuanEmployeeService;

    private final JwtUtil jwtUtil;

    public EmployeeController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/createAkunEmplooyee")
    public ResponseEntity<UsersEmployee> createCustomer(@RequestBody UserEmployeUsersRequest usersEmployee) {

        UsersEmployee savedEmployee = employeeService.addEmployee(usersEmployee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }
    @GetMapping("/getallEmployee")
    public List<UsersEmployee> getallEmployee() {
        return employeeService.getallEmployees();


    }

    @GetMapping("/getPengajuanEmployee")
    public List<pengajuan_userEmployee> getPengajuan(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Hapus "Bearer "

        UUID userId = UUID.fromString(jwtUtil.extractidUser(token));
        return pengajuanEmployeeService.getpengajuan_userEmployee(userId);

    }

    @GetMapping("/getPengajuanEmployeeMarketing")
    public List<pengajuan_userEmployee> getPengajuanEmployeeMarketing(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Hapus "Bearer "

        UUID userId = UUID.fromString(jwtUtil.extractidUser(token));
        return pengajuanEmployeeService.getpengajuanEmployeeMarketing(userId);

    }
    @GetMapping("/getPengajuanEmployeeBranchmanager")
    public List<pengajuan_userEmployee> getPengajuanEmployeeBranchmanager(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Hapus "Bearer "

        UUID userId = UUID.fromString(jwtUtil.extractidUser(token));
        return pengajuanEmployeeService.getpengajuanEmployeeBranchmanager(userId);

    }

    @GetMapping("/getPengajuanEmployeeBackoffice")
    public List<pengajuan_userEmployee> getPengajuanEmployeeBackoffice(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Hapus "Bearer "

        UUID userId = UUID.fromString(jwtUtil.extractidUser(token));
        return pengajuanEmployeeService.getpengajuanEmployeeBackoffice(userId);

    }

    @PostMapping("/recomendMarketing")
    public pengajuan_userEmployee recomendMarketing(@RequestHeader("Authorization") String authHeader
            ,@RequestBody PengajuanRequest pengajuanRequest) {
        String token =  authHeader.substring(7);
        return employeeService.recomendMarketing(token,pengajuanRequest.getId_pengajuan());
    }

    @PostMapping("/approveBranchManager")
    public pengajuan_userEmployee approveBranchManager(@RequestHeader("Authorization") String authHeader
            ,@RequestBody PengajuanRequest pengajuanRequest) {
        String token = authHeader.substring(7);
        return employeeService.approveBranchManager(token,pengajuanRequest.getId_pengajuan());
    }

    @PostMapping("/disbursement")
    public Pengajuan disbursement(@RequestBody PengajuanRequest pengajuanRequest) {
        return employeeService.disburseBackOffice(pengajuanRequest.getId_pengajuan());
    }

    @GetMapping ("/getProfileMarketing")
    public UsersEmployee getProfileMarketing(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        return employeeService.getEmployeeProfileFromToken(token);
    }
    @PostMapping("/getNipFromtoken")
    public NipnameRequest getNipFromtoken(@RequestHeader("Authorization") String authHeader) {
        String token =  authHeader.substring(7);
        System.out.println("Token diterima dari front end: " + token); // Debugging
        UsersEmployee usersEmployee= employeeService.getEmployeeProfileFromToken(token);

        NipnameRequest nipnameRequest = new NipnameRequest();
        nipnameRequest.setNama(usersEmployee.getUsers().getNama());
        nipnameRequest.setNip(usersEmployee.getNip());
        return nipnameRequest;
    }

    @GetMapping("/getAllEmoloyeeNipName")
    public List<NipnameRequest> getAllEmoloyeeNipName(@RequestHeader("Authorization") String authHeader) {
        String token =  authHeader.substring(7);
        return employeeService.getAllEmployeeNipName(token);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordEmployeeRequest request) {
        try {
            employeeService.updatePasswordEmployee(request);
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setMessage("Successfully updated password");
            return ResponseEntity.ok(responseMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileEmployeeRequest request) {
        try {
            UsersEmployee updated = employeeService.updateProfileEmployee(request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/ubahPassword")
    public ResponseEntity<?> ubahpassword(@RequestHeader("Authorization") String authHeader,@RequestBody UpdatePasswordUserRequest request) {
        String token = authHeader.substring(7);
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            employeeService.ubahPassword(request,token);

            responseMessage.setMessage("Successfully updated password");
            return ResponseEntity.ok(responseMessage);
        } catch (RuntimeException e) {
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
        }
    }



}
