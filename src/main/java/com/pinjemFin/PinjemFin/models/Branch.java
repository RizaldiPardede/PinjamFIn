package com.pinjemFin.PinjemFin.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_branch;

    @Column(nullable = false)
    private String nama_branch;

    @Column(nullable = false)
    private String alamat_branch;

    @Column(nullable = false)
    private Double latitude_branch;

    @Column(nullable = false)
    private Double longitude_branch;


}
