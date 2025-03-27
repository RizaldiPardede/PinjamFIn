package com.pinjemFin.PinjemFin.dto;


import com.pinjemFin.PinjemFin.models.Branch;
import com.pinjemFin.PinjemFin.models.Role;
import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.models.UsersEmployee;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.UUID;


@Data
public class UserEmployeUsersRequest {

    private Usersreq users;
    private Integer nip;
    private String jabatan;
    private UUID idbranch;


}


