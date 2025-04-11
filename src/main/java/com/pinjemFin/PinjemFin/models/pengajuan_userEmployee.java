package com.pinjemFin.PinjemFin.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pengajuan_userEmployee")
public class pengajuan_userEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_pengajuan_userEmployee;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "id_user_employee")
    private UsersEmployee id_user_employee;

    @ManyToOne
    @JoinColumn(name="id_pengajuan")
    private Pengajuan id_pengajuan;
}
