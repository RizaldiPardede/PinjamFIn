package com.pinjemFin.PinjemFin.dto;

import com.pinjemFin.PinjemFin.models.Branch;
import com.pinjemFin.PinjemFin.models.Users;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateEmployeeRequest {
    private UUID id_user_employee;
    private Users users;
    private int nip;
    private String jabatan;
    private Branch branch;
}
