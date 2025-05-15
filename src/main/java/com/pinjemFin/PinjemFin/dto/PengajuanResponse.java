package com.pinjemFin.PinjemFin.dto;

import lombok.Data;

@Data
public class PengajuanResponse {
    private Double amount;
    private Integer tenor;
    private Double angsuran;
    private Double bunga;
    private Double total_payment;
}
