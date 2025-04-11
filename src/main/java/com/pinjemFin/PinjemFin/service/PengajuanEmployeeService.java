package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.models.pengajuan_userEmployee;
import com.pinjemFin.PinjemFin.repository.EmployeeRepository;
import com.pinjemFin.PinjemFin.repository.PengajuanEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PengajuanEmployeeService {

    @Autowired
    private PengajuanEmployeeRepository pengajuanEmployeeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


    public pengajuan_userEmployee getpengajuan_userEmployee(UUID id_user) {
        UUID idUserEmployee = employeeRepository.findUsersEmployeeByUsersId(id_user).get().getId_user_employee();
        return pengajuanEmployeeRepository.findpengajuan_userEmployeeBy(idUserEmployee)
                .orElseThrow(() -> new RuntimeException("Data pengajuan_userEmployee tidak ditemukan"));
    }





}
