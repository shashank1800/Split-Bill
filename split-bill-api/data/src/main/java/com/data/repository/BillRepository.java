package com.data.repository;


import com.data.entity.BillEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Integer> {
    List<BillEntity> findAllByGroupId(Integer groupId, Sort sort);
    BillEntity findOneById(Integer id);
}