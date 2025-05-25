package com.pinjemFin.PinjemFin.repository;

import com.pinjemFin.PinjemFin.models.Pinjaman;
import com.pinjemFin.PinjemFin.models.UsersEmployee;
import com.pinjemFin.PinjemFin.models.pengajuan_userEmployee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PengajuanEmployeeRepository extends JpaRepository<pengajuan_userEmployee, UUID> {
//    @Query("SELECT pue.id_user_employee FROM pengajuan_userEmployee pue " +
//            "JOIN UsersEmployee ue ON pue.id_user_employee = ue.id_user_employee " +
//            "JOIN Pengajuan p ON pue.id_pengajuan = p.id_pengajuan " +
//            "WHERE ue.jabatan = 'marketing' " +
//            "AND ue.branch.id_branch = :idBranch " +
//            "GROUP BY pue.id_user_employee " +
//            "ORDER BY COUNT(pue.id_pengajuan) ASC")
//    List<UUID> findMarketing(@Param("idBranch") UUID idBranch, Pageable pageable);

//    @Query("SELECT pue.id_user_employee FROM pengajuan_userEmployee pue " +
//            "WHERE pue.id_user_employee.jabatan = 'marketing' " +
//            "AND pue.id_user_employee.branch.id_branch = :idBranch " +
//            "GROUP BY pue.id_user_employee " +
//            "ORDER BY COUNT(pue.id_pengajuan) ASC")
//    List<UsersEmployee> findMarketing(@Param("idBranch") UUID idBranch, Pageable pageable);

    @Query("SELECT ue FROM UsersEmployee ue " +
            "LEFT JOIN ue.pengajuanUserEmployees pue " +
            "WHERE ue.jabatan = 'Marketing' " +
            "AND ue.branch.id_branch = :idBranch " +
            "GROUP BY ue " +
            "ORDER BY COUNT(pue) ASC")
    List<UsersEmployee> findAvailableMarketing(@Param("idBranch") UUID idBranch, Pageable pageable);


    @Query ("SELECT pue FROM pengajuan_userEmployee pue " +
            "WHERE pue.id_user_employee.id_user_employee = :id_useremployee")
    List<pengajuan_userEmployee> findpengajuan_userEmployeeBy(@Param("id_useremployee") UUID id_user_employee);


    @Query ("SELECT pue FROM pengajuan_userEmployee pue " +
            "WHERE pue.id_user_employee.id_user_employee = :id_useremployee " +
            "AND pue.id_pengajuan.status = 'bckt_marketing' " +
            "AND pue.id_user_employee.users.role.nama_role = 'marketing' ")
    List<pengajuan_userEmployee> findpengajuan_userEmployeeMarketing(@Param("id_useremployee") UUID id_user_employee);

    @Query ("SELECT pue FROM pengajuan_userEmployee pue " +
            "WHERE pue.id_user_employee.id_user_employee = :id_useremployee " +
            "AND pue.id_pengajuan.status = 'bckt_BranchManager' " +
            "AND pue.id_user_employee.users.role.nama_role = 'branch manager'")
    List<pengajuan_userEmployee> findpengajuan_userEmployeeBranchmanager(@Param("id_useremployee") UUID id_user_employee);

    @Query ("SELECT pue FROM pengajuan_userEmployee pue " +
            "WHERE pue.id_user_employee.id_user_employee = :id_useremployee " +
            "AND pue.id_pengajuan.status = 'bckt_Operation' " +
            "AND pue.id_user_employee.users.role.nama_role = 'back office'")
    List<pengajuan_userEmployee> findpengajuan_userEmployeeBackoffice(@Param("id_useremployee") UUID id_user_employee);

    @Query("SELECT pue FROM pengajuan_userEmployee pue " +
            "WHERE pue.id_pengajuan.id_pengajuan = :idPengajuan")
    List<pengajuan_userEmployee> findByIdPengajuan(@Param("idPengajuan") UUID id_pengajuan);


    @Query("SELECT pue FROM pengajuan_userEmployee pue " +
            "WHERE pue.id_user_employee.id_user_employee = :idUserEmployee " +
            "AND pue.id_pengajuan.id_pengajuan = :idPengajuan")
    Optional<pengajuan_userEmployee> findByUserEmployeeAndPengajuan(@Param("idUserEmployee") UUID idUserEmployee, @Param("idPengajuan") UUID idPengajuan);

    @Query("SELECT pue FROM pengajuan_userEmployee pue " +
            "JOIN pue.id_user_employee.users.role r " +
            "WHERE pue.id_pengajuan.id_pengajuan = :idPengajuan " +
            "ORDER BY CASE r.nama_role " +
            "  WHEN 'marketing' THEN 1 " +
            "  WHEN 'branch manager' THEN 2 " +
            "  WHEN 'back office' THEN 3 " +
            "  ELSE 4 END")
    List<pengajuan_userEmployee> findByIdPengajuanOrderByRole(@Param("idPengajuan") UUID id_pengajuan);
}
