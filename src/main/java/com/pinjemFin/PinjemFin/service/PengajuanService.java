package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.dto.PengajuanCustomerRequest;
import com.pinjemFin.PinjemFin.dto.SimulasiPengajuanCustomerRequest;
import com.pinjemFin.PinjemFin.models.*;
import com.pinjemFin.PinjemFin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PengajuanService {

    @Autowired
    private PengajuanRepository pengajuanRepository;
    @Autowired
    private PlafonRepository plafonRepository;
    @Autowired
    private PengajuanEmployeeRepository pengajuanUserEmployeeRepository;

    @Autowired
    private EmployeeRepository usersEmployeeRepository;

    @Autowired
    private CustomerService customerService;

    @Transactional
    public Pengajuan createPengajuan(PengajuanCustomerRequest pengajuanCustomerRequest,String authHeader) {
        String token = authHeader.substring(7);
        Pengajuan pengajuan = new Pengajuan();
        pengajuan.setAmount(pengajuanCustomerRequest.getAmount());
        pengajuan.setTenor(pengajuanCustomerRequest.getTenor());
        pengajuan.setStatus("bckt_marketing");
        SimulasiPengajuanCustomerRequest hitungPengajuanCustomerRequest = getSimulasiPengajuan(
                pengajuan.getAmount(),pengajuan.getTenor(),token);

        pengajuan.setAngsuran(hitungPengajuanCustomerRequest.getAngsuran());
        pengajuan.setBunga(hitungPengajuanCustomerRequest.getBunga());
        pengajuan.setTotal_payment(hitungPengajuanCustomerRequest.getTotal_payment());
        pengajuan.setId_user_customer(customerService.getUserCustomer(customerService.getUserCustomerIdFromToken(token)));

        List<String> statusPending = List.of("bckt_marketing", "bckt_BranchManager", "bckt_Operation");
        boolean hasPending = pengajuanRepository.existsByUserAndStatusIn(pengajuan.getId_user_customer(), statusPending);

        if (hasPending) {
            throw new RuntimeException("Masih ada pengajuan yang belum selesai direview.");
        }
        else {
            //Simpan pengajuan baru
            pengajuanRepository.save(pengajuan);

        }

        //Cari marketing dengan pengajuan paling sedikit di branch yang sama
        Pageable pageable = (Pageable) PageRequest.of(0, 1);
        List<UsersEmployee> leastBusyMarketing = pengajuanUserEmployeeRepository.findAvailableMarketing(
                pengajuan.getId_user_customer().getBranch().getId_branch(), pageable);

        if (!leastBusyMarketing.isEmpty()) {


            //Buat entri di tabel pengajuan_user_employee
            pengajuan_userEmployee assignment = new pengajuan_userEmployee();
            assignment.setId_pengajuan(pengajuan);
            assignment.setId_user_employee(usersEmployeeRepository.findById(leastBusyMarketing.get(0).getId_user_employee()).get());
            pengajuanUserEmployeeRepository.save(assignment);
            UsersCustomer usersCustomer = customerService.getUserCustomer(customerService.getUserCustomerIdFromToken(token));
            usersCustomer.setSisa_plafon(usersCustomer.getSisa_plafon()-pengajuan.getAmount());
            customerService.saveCustomer(usersCustomer);
            return pengajuan;
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

    public SimulasiPengajuanCustomerRequest getSimulasiPengajuan(Double amount, int tenor, String token) {
        //cek agar yang belum melengkapi data tidak terkena error ketika mencoba simulasi
        UUID cekusercustomer;
        try {
            cekusercustomer = customerService.getUserCustomerIdFromToken(token);
        } catch (RuntimeException e) {
            cekusercustomer = null;
        }

        if (token == null || token.isEmpty()|| cekusercustomer == null) {
            SimulasiPengajuanCustomerRequest simulasiPengajuanCustomerRequest = new SimulasiPengajuanCustomerRequest();
            Plafon plafon = plafonRepository.findPlafonByJenis_plafon("Bronze").get();

            simulasiPengajuanCustomerRequest.setAmount(amount);
            simulasiPengajuanCustomerRequest.setTenor(tenor);
            simulasiPengajuanCustomerRequest.setBunga(plafon.getBunga());
            Double totalPengajuan = amount+(amount*(plafon.getBunga()/100));
            simulasiPengajuanCustomerRequest.setTotal_payment(totalPengajuan);
            simulasiPengajuanCustomerRequest.setAngsuran(totalPengajuan/tenor);
            if(amount>plafon.getJumlah_plafon()){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Maximal Pinjaman Plafon Anda Sekarang Rp "+plafon.getJumlah_plafon()
                );
            }
            return simulasiPengajuanCustomerRequest;
        }else {
            SimulasiPengajuanCustomerRequest simulasiPengajuanCustomerRequest = new SimulasiPengajuanCustomerRequest();
            Plafon plafon = customerService.getPlafon(token).getPlafon();

            simulasiPengajuanCustomerRequest.setAmount(amount);
            simulasiPengajuanCustomerRequest.setTenor(tenor);
            simulasiPengajuanCustomerRequest.setBunga(plafon.getBunga());

            Double totalPengajuan = amount+(amount*(plafon.getBunga()/100));
            simulasiPengajuanCustomerRequest.setTotal_payment(totalPengajuan);
            simulasiPengajuanCustomerRequest.setAngsuran(totalPengajuan/tenor);

            if(amount>plafon.getJumlah_plafon()){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Maximal Pinjaman Plafon Anda Sekarang Rp "+plafon.getJumlah_plafon()
                );
            }

            return simulasiPengajuanCustomerRequest;

        }

    }

    public List<Pengajuan> getAllPengajuanByCustomerId(UUID idCustomer) {
        return pengajuanRepository.findAllByCustomerId(idCustomer);
    }


}
