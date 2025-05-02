package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.dto.NipnameRequest;
import com.pinjemFin.PinjemFin.dto.UpdatePasswordEmployeeRequest;
import com.pinjemFin.PinjemFin.dto.UpdateProfileEmployeeRequest;
import com.pinjemFin.PinjemFin.dto.UserEmployeUsersRequest;
import com.pinjemFin.PinjemFin.models.*;
import com.pinjemFin.PinjemFin.repository.*;
import com.pinjemFin.PinjemFin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Autowired
    PengajuanRepository pengajuanRepository;

    @Autowired
    PengajuanEmployeeRepository pengajuanEmployeeRepository;

    @Autowired
    EmailService emailService;

    private final JwtUtil jwtUtil;

    public EmployeeService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public UsersEmployee addEmployee(UserEmployeUsersRequest usersEmployeeUsersRequest) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Generate raw password (8 karakter acak)
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);

        // Set data user
        Users users = new Users();
        users.setEmail(usersEmployeeUsersRequest.getUsers().getEmail());
        users.setPassword(passwordEncoder.encode(rawPassword)); // Simpan hashed password
        users.setNama(usersEmployeeUsersRequest.getUsers().getNama());
        users.setRole(roleRepository.findById(usersEmployeeUsersRequest.getUsers().getId_role()).get());
        Users usersaved = usersRepository.save(users);

        // Set data employee
        UsersEmployee usersEmployee = new UsersEmployee();
        usersEmployee.setNip(usersEmployeeUsersRequest.getNip());
        usersEmployee.setJabatan(usersEmployeeUsersRequest.getJabatan());
        usersEmployee.setUsers(usersaved);
        usersEmployee.setBranch(branchRepository.findById(usersEmployeeUsersRequest.getIdbranch()).get());

        UsersEmployee savedEmployee = employeeRepository.save(usersEmployee);

        // Kirim email setelah akun berhasil dibuat
        String subject = "Pembuatan Akun Berhasil";
        String body = "Pembuatan akun berhasil.\n\nBerikut data akun Anda:\n" +
                "NIP: " + savedEmployee.getNip() + "\n" +
                "Password: " + rawPassword + "\n\n" +
                "Segera ganti password Anda setelah login.";

        emailService.sendEmail(users.getEmail(), subject, body);

        return savedEmployee;
    }
    @Transactional
    public void updatePasswordEmployee(UpdatePasswordEmployeeRequest request) {
        UsersEmployee employee = employeeRepository.findByNip(request.getNip())
                .orElseThrow(() -> new RuntimeException("NIP tidak ditemukan"));

        Users user = employee.getUsers();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(request.getNewPassword()));

        usersRepository.save(user);
    }

    @Transactional
    public UsersEmployee updateProfileEmployee(UpdateProfileEmployeeRequest request) {
        UsersEmployee employee = employeeRepository.findByNip(request.getNip())
                .orElseThrow(() -> new RuntimeException("Employee tidak ditemukan"));

        Users user = employee.getUsers();

        user.setNama(request.getNama());
        user.setEmail(request.getEmail());
        user.setRole(roleRepository.findById(request.getId_role())
                .orElseThrow(() -> new RuntimeException("Role tidak ditemukan")));
        usersRepository.save(user);

        employee.setJabatan(request.getJabatan());
        employee.setBranch(branchRepository.findById(request.getId_branch())
                .orElseThrow(() -> new RuntimeException("Branch tidak ditemukan")));

        return employeeRepository.save(employee);
    }


    public List<UsersEmployee> getallEmployees() {
        return employeeRepository.findAll();
    }



    public Optional<UsersEmployee> getUsersEmployee(Integer nip) {

        return employeeRepository.findByNip(nip);
    }
    public UUID getUserEmployeeIdFromToken(String token) {
        // Ambil id_user langsung dari token JWT
        UUID idUser = UUID.fromString(jwtUtil.extractidUser(token));

        // Cari id_user_customer berdasarkan id_user
        return employeeRepository.findUsersEmployeeByUsersId(idUser)
                .map(UsersEmployee::getId_user_employee)
                .orElseThrow(() -> new RuntimeException("User Employee not found"));
    }

    public UsersEmployee getEmployeeProfileFromToken(String token) {
        UUID UsersId = getUserEmployeeIdFromToken(token);
        return employeeRepository.findById(UsersId)
                .orElseThrow(() -> new RuntimeException("Emoloyee not found"));
    }

    public List<NipnameRequest> getAllEmployeeNipName(String token) {

        List<UsersEmployee> usersEmployees  = employeeRepository.findAll();
        List<NipnameRequest> nipnameRequests = new ArrayList<>();

        for (UsersEmployee usersEmployee : usersEmployees) {
            NipnameRequest request = new NipnameRequest();
            request.setNama(usersEmployee.getUsers().getNama());
            request.setNip(usersEmployee.getNip());

            nipnameRequests.add(request); // tambahkan ke list
        }

        return nipnameRequests;
    }





    public pengajuan_userEmployee recomendMarketing(String token, UUID pengajuanId) {
        UUID marketingid = getUserEmployeeIdFromToken(token);
        UsersEmployee marketing = employeeRepository.findById(marketingid)
                .orElseThrow(() -> new RuntimeException("marketingid not found"));
        UsersEmployee branchmarketing = employeeRepository.findBranchManager(marketing.getBranch().getId_branch()
                        ,UUID.fromString("55861664-637A-402E-8BDF-F699E0E17889"))
                .orElseThrow(() -> new RuntimeException("branchmarketingid not found"));


        Optional<Pengajuan> pengajuan = pengajuanRepository.findById(pengajuanId);
        Pengajuan recomendfromMarketing = pengajuan
                .orElseThrow(() -> new RuntimeException("Pengajuan not found"));

        pengajuan_userEmployee pengajuan_userEmployee = new pengajuan_userEmployee();
        pengajuan_userEmployee.setId_user_employee(branchmarketing);
        pengajuan_userEmployee.setId_pengajuan(recomendfromMarketing);

        pengajuanRepository.updateStatusById(recomendfromMarketing.getId_pengajuan(),"bckt_BranchManager");
        return pengajuanEmployeeRepository.save(pengajuan_userEmployee);
    }

    public pengajuan_userEmployee approveBranchManager(String token, UUID pengajuanId) {
        UUID branchmanagerid = getUserEmployeeIdFromToken(token);
        UsersEmployee branchmanager = employeeRepository.findById(branchmanagerid)
                .orElseThrow(() -> new RuntimeException("branchmanager not found"));
        UsersEmployee backoffice = employeeRepository.findBackOffice(branchmanager.getBranch().getId_branch()
                ,UUID.fromString("613C6352-5168-407C-9C75-5759472E2CD7"))
                .orElseThrow(() -> new RuntimeException("backoffice not found"));

        Optional<Pengajuan> pengajuan = pengajuanRepository.findById(pengajuanId);
        Pengajuan recomendfromMarketing = pengajuan
                .orElseThrow(() -> new RuntimeException("Pengajuan not found"));

        pengajuan_userEmployee pengajuan_userEmployee = new pengajuan_userEmployee();
        pengajuan_userEmployee.setId_user_employee(backoffice);
        pengajuan_userEmployee.setId_pengajuan(recomendfromMarketing);

        pengajuanRepository.updateStatusById(recomendfromMarketing.getId_pengajuan(),"bckt_Operation");
        return pengajuanEmployeeRepository.save(pengajuan_userEmployee);
    }

    public Pengajuan disburseBackOffice(UUID pengajuanId) {
        Optional<Pengajuan> pengajuan = pengajuanRepository.findById(pengajuanId);
        Pengajuan ApprovetoDisburse = pengajuan.get();
        pengajuanRepository.updateStatusById(ApprovetoDisburse.getId_pengajuan(),"Disbursment");

        return ApprovetoDisburse;
    }









}
