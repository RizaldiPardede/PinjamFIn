package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.dto.NoteResponse;
import com.pinjemFin.PinjemFin.models.pengajuan_userEmployee;
import com.pinjemFin.PinjemFin.repository.EmployeeRepository;
import com.pinjemFin.PinjemFin.repository.PengajuanEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PengajuanEmployeeService {

    @Autowired
    private PengajuanEmployeeRepository pengajuanEmployeeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


    public List<pengajuan_userEmployee> getpengajuan_userEmployee(UUID id_user) {
        UUID idUserEmployee = employeeRepository.findUsersEmployeeByUsersId(id_user).get().getId_user_employee();
        List<pengajuan_userEmployee> list = pengajuanEmployeeRepository.findpengajuan_userEmployeeBy(idUserEmployee);

        if (list.isEmpty()) {
            throw new RuntimeException("Data pengajuan_userEmployee tidak ditemukan");
        }

        return list;
    }

    public List<pengajuan_userEmployee> getpengajuanEmployeeMarketing(UUID id_user) {
        UUID idUserEmployee = employeeRepository.findUsersEmployeeByUsersId(id_user).get().getId_user_employee();
        List<pengajuan_userEmployee> list = pengajuanEmployeeRepository.findpengajuan_userEmployeeMarketing(idUserEmployee);

        if (list.isEmpty()) {
            throw new RuntimeException("Data pengajuan_userEmployee tidak ditemukan");
        }

        return list;
    }

    public List<pengajuan_userEmployee> getpengajuanEmployeeBranchmanager(UUID id_user) {
        UUID idUserEmployee = employeeRepository.findUsersEmployeeByUsersId(id_user).get().getId_user_employee();
        List<pengajuan_userEmployee> list = pengajuanEmployeeRepository.findpengajuan_userEmployeeBranchmanager(idUserEmployee);

        if (list.isEmpty()) {
            throw new RuntimeException("Data pengajuan_userEmployee tidak ditemukan");
        }

        return list;
    }

    public List<pengajuan_userEmployee> getpengajuanEmployeeBackoffice(UUID id_user) {
        UUID idUserEmployee = employeeRepository.findUsersEmployeeByUsersId(id_user).get().getId_user_employee();
        List<pengajuan_userEmployee> list = pengajuanEmployeeRepository.findpengajuan_userEmployeeBackoffice(idUserEmployee);

        if (list.isEmpty()) {
            throw new RuntimeException("Data pengajuan_userEmployee tidak ditemukan");
        }

        return list;
    }

    public List<NoteResponse> getNote(UUID id_pengajuan) {
        List<pengajuan_userEmployee> pengajuanUserEmployees = pengajuanEmployeeRepository.findByIdPengajuanOrderByRole(id_pengajuan);

        List<NoteResponse> noteResponses = new ArrayList<>();
        if (pengajuanUserEmployees.get(0).getId_pengajuan().getStatus().equalsIgnoreCase("tolak")) {
            for (int i = 0; i < pengajuanUserEmployees.size(); i++) {
                pengajuan_userEmployee pue = pengajuanUserEmployees.get(i);
                NoteResponse noteResponse = new NoteResponse();
                noteResponse.setNama(pue.getId_user_employee().getUsers().getNama());
                noteResponse.setNama_role(pue.getId_user_employee().getUsers().getRole().getNama_role());
                if (i == pengajuanUserEmployees.size() - 1) {
                    noteResponse.setStatus("Reject");
                } else {
                    noteResponse.setStatus("Approve");
                }
                noteResponse.setNote(pue.getNote());
                noteResponses.add(noteResponse);
            }
        }
        else if(pengajuanUserEmployees.get(0).getId_pengajuan().getStatus().equalsIgnoreCase("Disbursment")){
            for (int i = 0; i < pengajuanUserEmployees.size(); i++) {
                pengajuan_userEmployee pue = pengajuanUserEmployees.get(i);
                NoteResponse noteResponse = new NoteResponse();
                noteResponse.setNama(pue.getId_user_employee().getUsers().getNama());
                noteResponse.setNama_role(pue.getId_user_employee().getUsers().getRole().getNama_role());
                noteResponse.setStatus("Approve");
                noteResponse.setNote(pue.getNote());
                noteResponses.add(noteResponse);
            }
        }
        else{
            for (int i = 0; i < pengajuanUserEmployees.size(); i++) {
                pengajuan_userEmployee pue = pengajuanUserEmployees.get(i);
                NoteResponse noteResponse = new NoteResponse();
                noteResponse.setNama(pue.getId_user_employee().getUsers().getNama());
                noteResponse.setNama_role(pue.getId_user_employee().getUsers().getRole().getNama_role());
                if (i == pengajuanUserEmployees.size() - 1) {
                    noteResponse.setStatus("In review");
                } else {
                    noteResponse.setStatus("Approve");
                }
                noteResponse.setNote(pue.getNote());
                noteResponses.add(noteResponse);
            }
        }


        return noteResponses;
    }





}
