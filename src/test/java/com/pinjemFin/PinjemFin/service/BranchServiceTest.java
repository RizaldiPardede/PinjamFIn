package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.models.Branch;
import com.pinjemFin.PinjemFin.repository.BranchRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchService branchService;

    @Test
    void testGetNearestBranch_ReturnsNearest() {

        Branch branch1 = new Branch(
                UUID.randomUUID(),
                "Branch A",
                "Jl. Merdeka No.1",
                -6.2,
                106.8
        );

        Branch branch2 = new Branch(
                UUID.randomUUID(),
                "Branch B",
                "Jl. Sudirman No.2",
                -7.25,
                112.76
        );
        List<Branch> mockBranches = List.of(branch1, branch2);

        Mockito.when(branchRepository.findAll()).thenReturn(mockBranches);

        // Act: lokasi input di Bandung (-6.914744, 107.609810)
        Branch result = branchService.getNearestBranch(-6.914744, 107.609810);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Branch A", result.getNama_branch());
    }

    @Test
    void testGetNearestBranch_WhenNoBranches_ReturnsNull() {
        Mockito.when(branchRepository.findAll()).thenReturn(Collections.emptyList());

        Branch result = branchService.getNearestBranch(-6.914744, 107.609810);

        Assertions.assertNull(result);
    }
}