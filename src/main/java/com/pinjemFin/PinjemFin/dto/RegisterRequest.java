package com.pinjemFin.PinjemFin.dto;

import lombok.Data;

import java.util.UUID;
@Data
public class RegisterRequest {

    private String username;
    private String password;
    private String nama;
}
