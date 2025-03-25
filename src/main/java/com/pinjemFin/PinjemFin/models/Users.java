package com.pinjemFin.PinjemFin.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


//@MappedSuperclass
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
//@Inheritance(strategy = InheritanceType.JOINED) // Bisa diganti dengan SINGLE_TABLE atau TABLE_PER_CLASS

public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_user;

    @Column(nullable = false)
    private String nama;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;
}

