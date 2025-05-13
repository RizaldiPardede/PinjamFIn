package com.pinjemFin.PinjemFin.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UserCustomerImage")
public class UserCustomerImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_user_customer", referencedColumnName = "id_user_customer")
    private UsersCustomer userCustomer;

    private String imageType;  // Contoh: "KTP", "NPWP", "Kartu Keluarga"

    private String imageUrl;

    private LocalDateTime createdAt;
}
