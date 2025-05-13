package com.pinjemFin.PinjemFin.repository;

import com.pinjemFin.PinjemFin.models.UserCustomerImage;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsersCustomerImageRepository extends JpaRepository<UserCustomerImage, UUID> {
    Optional<UserCustomerImage> findByUserCustomerAndImageType(UsersCustomer user, String imageType);
}
