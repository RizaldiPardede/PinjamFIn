package com.pinjemFin.PinjemFin.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PengajuanToPeminjamanRequest {
    private UUID id_user_customer;
    private Double amount;
    private Double angsuran;
    private Integer tenor;
    private Double bunga;

}
