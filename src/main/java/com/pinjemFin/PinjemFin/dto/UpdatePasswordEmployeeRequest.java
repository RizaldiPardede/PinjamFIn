package com.pinjemFin.PinjemFin.dto;

import lombok.Data;

@Data
public class UpdatePasswordEmployeeRequest{
    private Integer nip;
    private String newPassword;
}
