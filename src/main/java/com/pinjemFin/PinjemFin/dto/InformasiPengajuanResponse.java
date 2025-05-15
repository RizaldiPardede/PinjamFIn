package com.pinjemFin.PinjemFin.dto;


import lombok.Data;

@Data
public class InformasiPengajuanResponse {

    private String jenis_plafon;
    private Double jumlah_plafon;
    private Double persentasilvup;
    private Double sisa_plafon;
    private Double jumlah_pinjaman;
    private Double jumlah_pinjamanLunas;
}
