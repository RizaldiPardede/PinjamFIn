package com.pinjemFin.PinjemFin.models;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UsersEmployee")
//@PrimaryKeyJoinColumn(name = "id_user") // Menghubungkan dengan User
public class UsersEmployee  {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_user_employee;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false, unique = true) // Relasi ke Users
    private Users users;

    @Column(nullable = false)
    private Integer nip;

    @Column(nullable = false)
    private String jabatan;

    @ManyToOne
    @JoinColumn(name = "id_branch")
    private Branch branch;
}
