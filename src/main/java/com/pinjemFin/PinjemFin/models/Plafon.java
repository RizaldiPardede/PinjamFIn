package com.pinjemFin.PinjemFin.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plafon")
public class Plafon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_plafon;

    @Column(nullable = false)
    private String jenis_plafon;

    @Column(nullable = false)
    private Double jumlah_plafon;
}
