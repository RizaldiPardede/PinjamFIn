package com.pinjemFin.PinjemFin.models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
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

    @OneToMany(mappedBy = "id_user_employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<pengajuan_userEmployee> pengajuanUserEmployees;


}
