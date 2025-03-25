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
@Table(name = "feature")
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_feature;

    @Column(nullable = false)
    private String feature_name;
}
