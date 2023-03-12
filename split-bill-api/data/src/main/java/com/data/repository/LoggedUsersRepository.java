package com.data.repository;


import com.data.entity.LoggedUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggedUsersRepository extends JpaRepository<LoggedUsersEntity, Integer> {
    LoggedUsersEntity findOneByUsername(String username);
}
