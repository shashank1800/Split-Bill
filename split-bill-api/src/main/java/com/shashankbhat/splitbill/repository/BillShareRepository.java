package com.shashankbhat.splitbill.repository;

import com.shashankbhat.splitbill.entity.BillShareEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillShareRepository extends JpaRepository<BillShareEntity, Integer> {
    List<BillShareEntity> findAllByBillId(Integer billId, Sort sort);
}