package com.pinjemFin.PinjemFin.repository;

import com.pinjemFin.PinjemFin.models.Branch;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID> {

}
