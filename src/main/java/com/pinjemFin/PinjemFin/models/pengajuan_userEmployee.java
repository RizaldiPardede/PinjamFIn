package com.pinjemFin.PinjemFin.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pengajuan_userEmployee")
public class pengajuan_userEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pengajuan_userEmployee;

    @ManyToOne
    @JoinColumn(name = "id_user_employee")
    private UsersEmployee id_user_employee;

    @ManyToOne
    @JoinColumn(name="id_pengajuan")
    private Pengajuan id_pengajuan;
}
