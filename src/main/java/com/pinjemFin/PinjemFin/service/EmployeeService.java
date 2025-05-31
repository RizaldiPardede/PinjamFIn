package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.dto.*;
import com.pinjemFin.PinjemFin.models.*;
import com.pinjemFin.PinjemFin.repository.*;
import com.pinjemFin.PinjemFin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    RoleService roleService;

    @Autowired
    CustomerService customerService;

    @Autowired
    TokenNotifikasiRepository tokenNotifikasiRepository;

    @Autowired
    PinjamanRepository pinjamanRepository;

    @Autowired
    TokenNotifikasiRepository tokenRepository;

    private final JwtUtil jwtUtil;
    @Autowired
    private TokenNotifikasiService tokenNotifikasiService;

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
        users.setIsActive(false);
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
    public void ubahPassword( UpdatePasswordUserRequest request, String token) {
        UsersEmployee usersEmployee = getEmployeeProfileFromToken(token);

        Users user = usersEmployee.getUsers();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password doesn't match");
        }
        else {
            user.setPassword(encoder.encode(request.getNewPassword()));
            user.setIsActive(true);
            usersRepository.save(user);
        }

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





    public pengajuan_userEmployee recomendMarketing(String token, UUID pengajuanId,String note) {
        UUID marketingid = getUserEmployeeIdFromToken(token);
        //pengecekan apakah employee memang memiliki pengajuan ini
        if (!marketingid.equals(pengajuanEmployeeRepository.findByIdPengajuanRole(
                pengajuanId,"marketing").get().getId_user_employee().getId_user_employee())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,  // 403 Forbidden karena akses ditolak
                    "Anda tidak memiliki akses untuk pengajuan ini"
            );
        }
        UsersEmployee marketing = employeeRepository.findById(marketingid)
                .orElseThrow(() -> new RuntimeException("marketingid not found"));
        UsersEmployee branchmarketing = employeeRepository.findBranchManager(marketing.getBranch().getId_branch()
                        ,roleService.getRoleWithNameRole("branch manager").getId_role())
                .orElseThrow(() -> new RuntimeException("branchmarketingid not found"));


        Optional<Pengajuan> pengajuan = pengajuanRepository.findById(pengajuanId);
        Pengajuan recomendfromMarketing = pengajuan
                .orElseThrow(() -> new RuntimeException("Pengajuan not found"));

        pengajuan_userEmployee pengajuan_userEmployee = new pengajuan_userEmployee();
        pengajuan_userEmployee.setId_user_employee(branchmarketing);
        pengajuan_userEmployee.setId_pengajuan(recomendfromMarketing);

        pengajuanRepository.updateStatusById(recomendfromMarketing.getId_pengajuan(),"bckt_BranchManager");

        pengajuan_userEmployee Marketingnote = new pengajuan_userEmployee();
        Marketingnote.setId_user_employee(marketing);
        Marketingnote.setId_pengajuan(recomendfromMarketing);
        Marketingnote.setNote(note);
        Marketingnote.setId_pengajuan_userEmployee(pengajuanEmployeeRepository.findByUserEmployeeAndPengajuan(marketingid,recomendfromMarketing.getId_pengajuan())
                .orElseThrow().getId_pengajuan_userEmployee());
        pengajuanEmployeeRepository.save(Marketingnote);
        return pengajuanEmployeeRepository.save(pengajuan_userEmployee);
    }

    public pengajuan_userEmployee approveBranchManager(String token, UUID pengajuanId,String note) {
        UUID branchmanagerid = getUserEmployeeIdFromToken(token);
        //pengecekan apakah employee memang memiliki pengajuan ini
        if (!branchmanagerid.equals(pengajuanEmployeeRepository.findByIdPengajuanRole(
                pengajuanId,"branch manager").get().getId_user_employee().getId_user_employee())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,  // 403 Forbidden karena akses ditolak
                    "Anda tidak memiliki akses untuk pengajuan ini"
            );
        }
        UsersEmployee branchmanager = employeeRepository.findById(branchmanagerid)
                .orElseThrow(() -> new RuntimeException("branchmanager not found"));
        UsersEmployee backoffice = employeeRepository.findBackOffice(branchmanager.getBranch().getId_branch()
                ,roleService.getRoleWithNameRole("back office").getId_role())
                .orElseThrow(() -> new RuntimeException("backoffice not found"));

        Optional<Pengajuan> pengajuan = pengajuanRepository.findById(pengajuanId);
        Pengajuan ApprovefromBM = pengajuan
                .orElseThrow(() -> new RuntimeException("Pengajuan not found"));

        pengajuan_userEmployee pengajuan_userEmployee = new pengajuan_userEmployee();
        pengajuan_userEmployee.setId_user_employee(backoffice);
        pengajuan_userEmployee.setId_pengajuan(ApprovefromBM);

        pengajuanRepository.updateStatusById(ApprovefromBM.getId_pengajuan(),"bckt_Operation");
        List<TokenNotifikasi>  tokenNotifikasis = tokenRepository.findTokensByCustomerId(pengajuan_userEmployee.getId_pengajuan().getId_user_customer().getId_user_customer());
        tokenNotifikasiService.sendNotificationToTokens(tokenNotifikasis,"Halo Ada info nih","Pengajuan Anda Rp."+pengajuan.get().getAmount()+" Telah Di Approve");

        pengajuan_userEmployee BMnote = new pengajuan_userEmployee();
        BMnote.setId_user_employee(branchmanager);
        BMnote.setId_pengajuan(ApprovefromBM);
        BMnote.setNote(note);
        BMnote.setId_pengajuan_userEmployee(pengajuanEmployeeRepository.findByUserEmployeeAndPengajuan(branchmanagerid,ApprovefromBM.getId_pengajuan())
                .orElseThrow().getId_pengajuan_userEmployee());
        pengajuanEmployeeRepository.save(BMnote);
        return pengajuanEmployeeRepository.save(pengajuan_userEmployee);
    }

    public pengajuan_userEmployee disburseBackOffice(String token,UUID pengajuanId,String note) {
        UUID backofficeid = getUserEmployeeIdFromToken(token);
        UsersEmployee backoffice = employeeRepository.findById(backofficeid)
                .orElseThrow(() -> new RuntimeException("branchmanager not found"));

        //pengecekan apakah employee memang memiliki pengajuan ini
        if (!backofficeid.equals(pengajuanEmployeeRepository.findByIdPengajuanRole(
                pengajuanId,"back office").get().getId_user_employee().getId_user_employee())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,  // 403 Forbidden karena akses ditolak
                    "Anda tidak memiliki akses untuk pengajuan ini"
            );
        }
        Optional<Pengajuan> pengajuan = pengajuanRepository.findById(pengajuanId);
        Pengajuan ApprovetoDisburse = pengajuan.orElseThrow();
        UsersCustomer usersCustomer = ApprovetoDisburse.getId_user_customer();
        pengajuanRepository.updateStatusById(ApprovetoDisburse.getId_pengajuan(),"Disbursment");



        //karna telah di disburse masuk table pinjaman
        Pinjaman pinjaman = new Pinjaman();
        pinjaman.setId_user_customer(ApprovetoDisburse.getId_user_customer());
        pinjaman.setBunga(ApprovetoDisburse.getBunga());
        pinjaman.setAngsuran(ApprovetoDisburse.getAngsuran());
        pinjaman.setTenor(ApprovetoDisburse.getTenor());
        pinjaman.setJumlah_pinjaman(ApprovetoDisburse.getAmount());
        pinjaman.setSisa_tenor(ApprovetoDisburse.getTenor());
        pinjaman.setSisa_pokok_hutang(ApprovetoDisburse.getAmount());
        pinjaman.setTotal_payment(ApprovetoDisburse.getTotal_payment());
        pinjamanRepository.save(pinjaman);

        //set notenya
        pengajuan_userEmployee BackOfficeNote = new pengajuan_userEmployee();
        BackOfficeNote.setId_user_employee(backoffice);
        BackOfficeNote.setId_pengajuan(ApprovetoDisburse);
        BackOfficeNote.setNote(note);
        BackOfficeNote.setId_pengajuan_userEmployee(pengajuanEmployeeRepository.findByUserEmployeeAndPengajuan(backofficeid,ApprovetoDisburse.getId_pengajuan())
                .orElseThrow().getId_pengajuan_userEmployee());
        pengajuanEmployeeRepository.save(BackOfficeNote);


        List<TokenNotifikasi>  tokenNotifikasis = tokenRepository.findTokensByCustomerId(usersCustomer.getId_user_customer());
        tokenNotifikasiService.sendNotificationToTokens(tokenNotifikasis,"Halo Segera Cek Saldo Anda","Pengajuan Anda Rp."+pengajuan.get().getAmount()+" Telah Di Disburse");
        return BackOfficeNote;
    }

    public Pengajuan reject(String token,UUID pengajuanId,String note) {
        UUID employeeid = getUserEmployeeIdFromToken(token);
        UsersEmployee employee = employeeRepository.findById(employeeid)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        //pengecekan double
        switch (employee.getUsers().getRole().getNama_role()) {
            case "marketing":
                if (!employeeid.equals(pengajuanEmployeeRepository.findByIdPengajuanRole(
                    pengajuanId,"marketing").get().getId_user_employee().getId_user_employee())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,  // 403 Forbidden karena akses ditolak
                        "Anda tidak memiliki akses untuk pengajuan ini"
                );
            }
                break;
            case "branch manager":
                if (!employeeid.equals(pengajuanEmployeeRepository.findByIdPengajuanRole(
                        pengajuanId,"branch manager").get().getId_user_employee().getId_user_employee())) {
                    throw new ResponseStatusException(
                            HttpStatus.FORBIDDEN,  // 403 Forbidden karena akses ditolak
                            "Anda tidak memiliki akses untuk pengajuan ini"
                    );
                }
                break;

            case "back office":
                if (!employeeid.equals(pengajuanEmployeeRepository.findByIdPengajuanRole(
                        pengajuanId,"back office").get().getId_user_employee().getId_user_employee())) {
                    throw new ResponseStatusException(
                            HttpStatus.FORBIDDEN,  // 403 Forbidden karena akses ditolak
                            "Anda tidak memiliki akses untuk pengajuan ini"
                    );
                }
                break;
        }

        Optional<Pengajuan> pengajuan = pengajuanRepository.findById(pengajuanId);
        Pengajuan reject = pengajuan.orElseThrow();
        UsersCustomer usersCustomer = reject.getId_user_customer();
        pengajuanRepository.updateStatusById(reject.getId_pengajuan(),"tolak");



        //set notenya
        pengajuan_userEmployee BackOfficeNote = new pengajuan_userEmployee();
        BackOfficeNote.setId_user_employee(employee);
        BackOfficeNote.setId_pengajuan(reject);
        BackOfficeNote.setNote(note);
        BackOfficeNote.setId_pengajuan_userEmployee(pengajuanEmployeeRepository.findByUserEmployeeAndPengajuan(employeeid,reject.getId_pengajuan())
                .orElseThrow().getId_pengajuan_userEmployee());
        pengajuanEmployeeRepository.save(BackOfficeNote);

        //pengembalian sisa plafon
        usersCustomer.setSisa_plafon(usersCustomer.getSisa_plafon()+reject.getAmount());
        customerService.saveCustomer(usersCustomer);

        List<TokenNotifikasi>  tokenNotifikasis = tokenRepository.findTokensByCustomerId(usersCustomer.getId_user_customer());
        tokenNotifikasiService.sendNotificationToTokens(tokenNotifikasis,"Halo Mohon Maaf","Pengajuan Anda Rp."+pengajuan.get().getAmount()+" Ditolak karena alasan kelengkapan data");
        return reject;
    }



    public List<UsersCustomer> getallcustomer(){
        return customerService.getAllCustomer();
    }





}
