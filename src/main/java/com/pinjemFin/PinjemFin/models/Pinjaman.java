package com.pinjemFin.PinjemFin.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pinjaman")

public class Pinjaman {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_pinjaman;

    @ManyToOne
    @JoinColumn(name = "id_user_customer")
    private UsersCustomer id_user_customer;

    @Column(nullable = false)
    private Double jumlah_pinjaman;

    @Column(nullable = false)
    private Integer tenor;

    @Column(nullable = false)
    private Double angsuran;

    @Column(nullable = false)
    private Double bunga;

    @Column(nullable = false)
    private Integer sisa_tenor;

    @Column(nullable = false)
    private Double sisa_pokok_hutang;


}
