package com.shashankbhat.splitbill.dto;

import java.time.LocalDateTime;


public class BillSharesDto {
    Integer id;
    Integer billId;
    Integer userId;
    Float spent;
    Float share;
    LocalDateTime dateCreated;
}
