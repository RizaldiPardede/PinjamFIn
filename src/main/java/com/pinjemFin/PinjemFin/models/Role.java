package com.pinjemFin.PinjemFin.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_role;

    @Column(nullable = false, unique = true)
    private String nama_role;

//    // Relasi OneToMany dengan Role_Feature
//    @OneToMany(mappedBy = "id_role", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Role_Feature> roleFeatures = new ArrayList<>();


}
