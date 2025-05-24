package com.pinjemFin.PinjemFin.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class GetAllDocumentRequest {
    private UUID id_user_customer;
}
