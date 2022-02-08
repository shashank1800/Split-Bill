package com.shashankbhat.splitbill.repository;

import com.shashankbhat.splitbill.entity.LoggedUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoggedUsersRepository extends JpaRepository<LoggedUsersEntity, Integer> {
    LoggedUsersEntity findOneByUsername(String username);
}
