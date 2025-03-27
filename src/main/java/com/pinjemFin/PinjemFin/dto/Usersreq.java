package com.pinjemFin.PinjemFin.dto;

import lombok.Data;

import java.util.UUID;

@Data
 public class Usersreq {
    private String email;
    private String password;
    private String nama;
    private UUID id_role;
}
