package com.shashankbhat.splitbill.repository;

import com.shashankbhat.splitbill.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
    List<UsersEntity> findByGroupId(Integer groupId);
}