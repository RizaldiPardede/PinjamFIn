package com.pinjemFin.PinjemFin.service;


import com.pinjemFin.PinjemFin.models.Branch;
import com.pinjemFin.PinjemFin.repository.BranchRepository;
import com.pinjemFin.PinjemFin.utils.LocationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BranchService {
    @Autowired
    BranchRepository branchRepository;


    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public Optional<Branch> getBranchById(UUID id) {
        return branchRepository.findById(id);
    }

    public Branch saveBranch(Branch branch) {
        return branchRepository.save(branch);
    }

    @Transactional
    public Optional<Branch> updatePartialBranch(UUID id, Map<String, Object> updates) {
        Optional<Branch> optionalBranch = branchRepository.findById(id);

        if (optionalBranch.isPresent()) {
            Branch branch = optionalBranch.get();

            updates.forEach((key, value) -> {
                switch (key) {
                    case "nama_branch":
                        branch.setNama_branch((String) value);
                        break;
                    case "alamat_branch":
                        branch.setAlamat_branch((String) value);
                        break;
                    case "latitude_branch":
                        branch.setLatitude_branch(Double.parseDouble(value.toString()));
                        break;
                    case "longitude_branch":
                        branch.setLatitude_branch(Double.parseDouble(value.toString()));
                        break;
                    default:
                        // Field tidak dikenali, bisa diabaikan atau dilempar error
                        break;
                }
            });

            return Optional.of(branchRepository.save(branch));
        }

        return Optional.empty();
    }


    public Branch getNearestBranch(double inputLat, double inputLon) {
        List<Branch> branches = branchRepository.findAll();

        return branches.stream()
                .min(Comparator.comparingDouble(
                        b -> LocationUtils.calculateDistance(inputLat, inputLon, b.getLatitude_branch(), b.getLongitude_branch())
                ))
                .orElse(null);
    }



}
