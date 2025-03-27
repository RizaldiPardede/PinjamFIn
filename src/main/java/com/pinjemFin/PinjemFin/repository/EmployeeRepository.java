package com.pinjemFin.PinjemFin.repository;

import com.pinjemFin.PinjemFin.models.UsersEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface EmployeeRepository extends JpaRepository<UsersEmployee, UUID> {

}
