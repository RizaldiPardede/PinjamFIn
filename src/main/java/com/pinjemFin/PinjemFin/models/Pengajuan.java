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
@Table(name = "pengajuan")
public class Pengajuan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_pengajuan;

    @ManyToOne
    @JoinColumn(name = "id_user_customer")
    private UsersCustomer id_user_customer;

    @Column(nullable = false)
    private String status;

    @Column(nullable=false)
    private Double amount;

    @Column(nullable=false)
    private Integer tenor;

    @Column(nullable=false)
    private Double angsuran;

    @Column(nullable = false)
    private Double bunga;

    @Column(nullable = true)
    private Double total_payment;
}
