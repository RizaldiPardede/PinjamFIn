package com.pinjemFin.PinjemFin.models;

import jakarta.persistence.*;
import lombok.*;

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

    public Role(String uuid, String customer) {
        this.id_role = UUID.fromString(uuid);
        this.nama_role = customer;
    }
}
