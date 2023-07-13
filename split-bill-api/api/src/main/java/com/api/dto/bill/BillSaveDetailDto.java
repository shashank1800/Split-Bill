package com.api.dto.bill;

import com.data.entity.BillEntity;
import com.data.entity.BillShareEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillSaveDetailDto {
    BillEntity bill;
    List<BillShareEntity> billShares;
}