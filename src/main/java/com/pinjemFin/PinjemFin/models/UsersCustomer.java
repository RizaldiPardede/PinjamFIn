package com.pinjemFin.PinjemFin.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UserCustomer")
//@PrimaryKeyJoinColumn(name = "id_user") // Menghubungkan dengan users
public class UsersCustomer  {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_user_customer;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false, unique = true) // Relasi ke Users
    private Users users;

    @Column(nullable = false)
    private String tempat_tgl_lahir;

    @Column(nullable = false)
    private String no_telp;

    @Column(nullable = false)
    private String alamat;

    @Column(nullable = false)
    private String nik;

    @Column(nullable = false)
    private String nama_ibu_kandung;

    @Column(nullable = false)
    private String pekerjaan;

    @Column(nullable = false)
    private String gaji;

    @Column(nullable = false)
    private String no_rek;

    @Column(nullable = false)
    private String status_rumah;

    @ManyToOne
    @JoinColumn(name = "id_plafon")
    private Plafon plafon;

    private Double sisa_plafon;
}
