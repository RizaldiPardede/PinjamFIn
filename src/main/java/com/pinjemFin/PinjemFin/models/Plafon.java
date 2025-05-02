package com.pinjemFin.PinjemFin.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plafon")
public class Plafon {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_plafon;

    @Column(nullable = false)
    private String jenis_plafon;

    @Column(nullable = false)
    private Double jumlah_plafon;

    @Column(nullable = false)
    private Double bunga;
}
