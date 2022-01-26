package com.shashankbhat.splitbill.repository;

import com.shashankbhat.splitbill.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Integer>{
    List<BillEntity> findAllByGroupId(Integer groupId);
}