package com.pinjemFin.PinjemFin.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateProfileEmployeeRequest {
    private Integer nip;
    private String nama;
    private String email;
    private String jabatan;
    private UUID id_branch;
    private UUID id_role;
}
