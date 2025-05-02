package com.pinjemFin.PinjemFin.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RoleFeatureRequest {
    private UUID id_role;
    private List<UUID> featureIds;
}
