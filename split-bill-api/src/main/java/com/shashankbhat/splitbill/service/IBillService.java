package com.shashankbhat.splitbill.service;

import com.shashankbhat.splitbill.dto.bill.BillAllDto;
import com.shashankbhat.splitbill.dto.bill.BillDto;
import com.shashankbhat.splitbill.dto.bill.BillSaveDetailDto;
import com.shashankbhat.splitbill.dto.bill.BillSaveDto;
import com.shashankbhat.exception.KnownException;

public interface IBillService {
    BillSaveDetailDto saveBill(BillSaveDto transaction, Integer uniqueId) throws Exception;

    BillAllDto getAllBills(Integer groupId) throws KnownException;

    BillDto deleteBills(BillDto billDto, Integer uniqueId) throws KnownException;
}
