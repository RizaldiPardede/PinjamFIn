package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.models.Pengajuan;
import com.pinjemFin.PinjemFin.models.UsersEmployee;
import com.pinjemFin.PinjemFin.models.pengajuan_userEmployee;
import com.pinjemFin.PinjemFin.repository.EmployeeRepository;
import com.pinjemFin.PinjemFin.repository.PengajuanEmployeeRepository;
import com.pinjemFin.PinjemFin.repository.PengajuanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PengajuanService {

    @Autowired
    private PengajuanRepository pengajuanRepository;

    @Autowired
    private PengajuanEmployeeRepository pengajuanUserEmployeeRepository;

    @Autowired
    private EmployeeRepository usersEmployeeRepository;

    @Transactional
    public pengajuan_userEmployee createPengajuan(Pengajuan pengajuan) {
        // 1. Simpan pengajuan baru
        pengajuanRepository.save(pengajuan);

        // 2. Cari marketing dengan pengajuan paling sedikit di branch yang sama
        Pageable pageable = (Pageable) PageRequest.of(0, 1);
        List<UsersEmployee> leastBusyMarketing = pengajuanUserEmployeeRepository.findAvailableMarketing(
                pengajuan.getId_user_customer().getBranch().getId_branch(), pageable);

        if (!leastBusyMarketing.isEmpty()) {


            // 3. Buat entri di tabel pengajuan_user_employee
            pengajuan_userEmployee assignment = new pengajuan_userEmployee();
            assignment.setId_pengajuan(pengajuan);
            assignment.setId_user_employee(usersEmployeeRepository.findById(leastBusyMarketing.get(0).getId_user_employee()).get());
            return pengajuanUserEmployeeRepository.save(assignment);
        }
        else {
            throw new RuntimeException("Tidak ada marketing yang tersedia untuk menangani pengajuan ini.");
        }
    }

    public Pengajuan getPengajuanById(UUID id) {
        Optional<Pengajuan> pengajuan = pengajuanRepository.findById(id);

        return pengajuan
                .orElseThrow(() -> new RuntimeException("Data pengajuan tidak ditemukan"));
    }
}
