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
@Table(name = "role_feature")
public class Role_Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_role_feature;

    @ManyToOne
    private Role id_role;

    @ManyToOne
    private Feature id_feature;
}
