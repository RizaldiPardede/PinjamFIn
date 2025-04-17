package com.pinjemFin.PinjemFin.controller;

import com.pinjemFin.PinjemFin.dto.PengajuanRequest;
import com.pinjemFin.PinjemFin.dto.UserEmployeUsersRequest;
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


}
