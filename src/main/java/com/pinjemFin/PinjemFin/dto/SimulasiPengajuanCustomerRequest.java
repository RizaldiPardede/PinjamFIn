package com.pinjemFin.PinjemFin.dto;

import com.pinjemFin.PinjemFin.models.UsersCustomer;
import jakarta.persistence.*;
import lombok.Data;




@Data
public class PengajuanCustomerRequest {


//    private UsersCustomer user_customer;
//    private String status;
    private Double amount;
    private Integer tenor;
    private Double angsuran;
    private Double bunga;
    private Double total_payment;
}
