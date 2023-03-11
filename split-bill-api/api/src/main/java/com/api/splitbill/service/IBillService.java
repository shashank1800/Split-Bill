package com.api.splitbill.service;

import com.common.exception.KnownException;
import com.api.splitbill.dto.bill.BillAllDto;
import com.api.splitbill.dto.bill.BillDto;
import com.api.splitbill.dto.bill.BillSaveDetailDto;
import com.api.splitbill.dto.bill.BillSaveDto;

public interface IBillService {
    BillSaveDetailDto saveBill(BillSaveDto transaction, Integer uniqueId) throws Exception;

    BillAllDto getAllBills(Integer groupId) throws KnownException;

    BillDto deleteBills(BillDto billDto, Integer uniqueId) throws KnownException;
}
