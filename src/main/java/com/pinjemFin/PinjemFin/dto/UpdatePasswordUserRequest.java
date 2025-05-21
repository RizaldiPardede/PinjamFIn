package com.pinjemFin.PinjemFin.dto;

import lombok.Data;

@Data
public class UpdatePasswordUserRequest {
    private String oldPassword;
    private String newPassword;
}
