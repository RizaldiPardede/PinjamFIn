package com.pinjemFin.PinjemFin.dto;

import com.pinjemFin.PinjemFin.models.Branch;
import com.pinjemFin.PinjemFin.models.Plafon;
import com.pinjemFin.PinjemFin.models.Users;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;
@Data
public class DetailCustomerRequest {


    private String tempat_tgl_lahir;
    private String no_telp;
    private String alamat;
    private String nik;
    private String nama_ibu_kandung;
    private String pekerjaan;
    private String gaji;
    private String no_rek;
    private String status_rumah;
    private Double latitude_alamat;
    private Double longitude_alamat;


}
