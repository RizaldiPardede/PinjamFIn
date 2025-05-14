package com.pinjemFin.PinjemFin.repository;

import com.pinjemFin.PinjemFin.models.UserCustomerImage;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UsersCustomerImageRepository extends JpaRepository<UserCustomerImage, UUID> {
    Optional<UserCustomerImage> findByUserCustomerAndImageType(UsersCustomer user, String imageType);

    @Query("SELECT i FROM UserCustomerImage i WHERE i.userCustomer.id_user_customer = :idCustomer AND i.imageType = 'profile'")
    Optional<UserCustomerImage> findProfileImageByCustomerId(@Param("idCustomer") UUID idCustomer);
}
