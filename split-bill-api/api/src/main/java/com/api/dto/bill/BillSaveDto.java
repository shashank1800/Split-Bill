package com.api.dto.bill;

import lombok.Data;

import java.util.List;

@Data
public class BillSaveDto {
    BillEntityDto bill;
    List<BillShareEntityDto> billShares;
}