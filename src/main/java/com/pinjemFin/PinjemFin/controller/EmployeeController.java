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
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_createEmployee')")
    @PostMapping("/createAkunEmplooyee")
    public ResponseEntity<UsersEmployee> createCustomer(@RequestBody UserEmployeUsersRequest usersEmployee) {

        UsersEmployee savedEmployee = employeeService.addEmployee(usersEmployee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_getAllEmployee')")
    @GetMapping("/getallEmployee")
    public List<UsersEmployee> getallEmployee() {
        return employeeService.getallEmployees();


    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_getPengajuanEmployee')")
    @GetMapping("/getPengajuanEmployee")
    public List<pengajuan_userEmployee> getPengajuan(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Hapus "Bearer "

        UUID userId = UUID.fromString(jwtUtil.extractidUser(token));
        return pengajuanEmployeeService.getpengajuan_userEmployee(userId);

    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_getPengajuanMarketing')")
    @GetMapping("/getPengajuanEmployeeMarketing")
    public List<pengajuan_userEmployee> getPengajuanEmployeeMarketing(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Hapus "Bearer "

        UUID userId = UUID.fromString(jwtUtil.extractidUser(token));
        return pengajuanEmployeeService.getpengajuanEmployeeMarketing(userId);

    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_getPengajuanBM')")
    @GetMapping("/getPengajuanEmployeeBranchmanager")
    public List<pengajuan_userEmployee> getPengajuanEmployeeBranchmanager(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Hapus "Bearer "

        UUID userId = UUID.fromString(jwtUtil.extractidUser(token));
        return pengajuanEmployeeService.getpengajuanEmployeeBranchmanager(userId);

    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_getPengajuanBackOffice')")
    @GetMapping("/getPengajuanEmployeeBackoffice")
    public List<pengajuan_userEmployee> getPengajuanEmployeeBackoffice(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Hapus "Bearer "

        UUID userId = UUID.fromString(jwtUtil.extractidUser(token));
        return pengajuanEmployeeService.getpengajuanEmployeeBackoffice(userId);

    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_recomendMarketing')")
    @PostMapping("/recomendMarketing")
    public pengajuan_userEmployee recomendMarketing(@RequestHeader("Authorization") String authHeader
            ,@RequestBody PengajuanRequest pengajuanRequest) {
        String token =  authHeader.substring(7);
        return employeeService.recomendMarketing(token,pengajuanRequest.getId_pengajuan(),pengajuanRequest.getNote());
    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_approveBM')")
    @PostMapping("/approveBranchManager")
    public pengajuan_userEmployee approveBranchManager(@RequestHeader("Authorization") String authHeader
            ,@RequestBody PengajuanRequest pengajuanRequest) {
        String token = authHeader.substring(7);
        return employeeService.approveBranchManager(token,pengajuanRequest.getId_pengajuan(),pengajuanRequest.getNote());
    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_disburse')")
    @PostMapping("/disbursement")
    public pengajuan_userEmployee disbursement(@RequestHeader("Authorization") String authHeader
            ,@RequestBody PengajuanRequest pengajuanRequest) {
        String token = authHeader.substring(7);
        return employeeService.disburseBackOffice(token,pengajuanRequest.getId_pengajuan(),pengajuanRequest.getNote());
    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_reject')")
    @PostMapping("/reject")
    public Pengajuan reject(@RequestHeader("Authorization") String authHeader
            ,@RequestBody PengajuanRequest pengajuanRequest) {
        String token = authHeader.substring(7);
        return employeeService.reject(token,pengajuanRequest.getId_pengajuan(),pengajuanRequest.getNote());
    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_getProfileEmployee')")
    @GetMapping ("/getProfileEmployee")
    public UsersEmployee getProfileMarketing(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        return employeeService.getEmployeeProfileFromToken(token);
    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_websocket')")
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

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_websocket')")
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

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_editEmployee')")
    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileEmployeeRequest request) {
        System.out.println("Received update profile request: " + request);
        try {
            UsersEmployee updated = employeeService.updateProfileEmployee(request);

            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_ubahPassword')")
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

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_getAllCustomer')")
    @GetMapping("/getAllCustomer")
    public ResponseEntity<?> getAllCustomer() {
        try {

            return ResponseEntity.ok(employeeService.getallcustomer());
        } catch (RuntimeException e) {
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
        }
    }

    @PreAuthorize("@permissionEvaluator.hasAccess(authentication, 'feature_getNote')")
    @PostMapping("/getNote")
    public List<NoteResponse> getNote(@RequestBody NoteRequest request) {
        return pengajuanEmployeeService.getNote(request.getId_pengajuan());
    }



}
