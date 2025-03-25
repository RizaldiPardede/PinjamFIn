package com.pinjemFin.PinjemFin.dto;

import lombok.Data;

import java.util.UUID;


@Data
public class CekUpdateAkunRequest {
    private String alamat;
    private String gaji;
    private String nama_ibu_kandung;
    private String pekerjaan;
    private String no_telp;
    private String no_rek;
    private String status_rumah;
    private String tempat_tgl_lahir;
    private String nik;
    private UUID id_plafon;
    private UUID id_user;
    private UUID id_user_customer;


}
