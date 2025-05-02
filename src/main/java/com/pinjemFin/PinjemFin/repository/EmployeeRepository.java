package com.pinjemFin.PinjemFin.repository;

import com.pinjemFin.PinjemFin.models.Branch;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import com.pinjemFin.PinjemFin.models.UsersEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface EmployeeRepository extends JpaRepository<UsersEmployee, UUID> {
    Optional<UsersEmployee> findByNip(Integer nip);
    List<UsersEmployee> findAll();
    UsersEmployee save(UsersEmployee usersEmployee);

    @Transactional(readOnly = true)
    @Query("SELECT ue FROM UsersEmployee ue WHERE ue.users.id_user = :idUser")
    Optional<UsersEmployee> findUsersEmployeeByUsersId(@Param("idUser") UUID idUser);


    @Query("SELECT ue FROM UsersEmployee ue " +
            "WHERE ue.users.role.id_role = :idRole " +
            "AND ue.branch.id_branch = :idbranch ")
    Optional<UsersEmployee> findBranchManager(@Param("idbranch") UUID idbranch ,@Param("idRole") UUID idRole);

    @Query("SELECT ue FROM UsersEmployee ue " +
            "WHERE ue.users.role.id_role = :idRole " +
            "AND ue.branch.id_branch = :idbranch ")
    Optional<UsersEmployee> findBackOffice(@Param("idbranch") UUID idbranch,@Param("idRole") UUID idRole);




}
