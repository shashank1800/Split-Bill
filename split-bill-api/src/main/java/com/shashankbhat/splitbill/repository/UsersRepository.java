package com.shashankbhat.splitbill.repository;

import com.shashankbhat.splitbill.entity.UsersEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
    @Query(value = "SELECT * FROM users  " +
            "WHERE group_id= ?1 " +
            "ORDER BY date_created", nativeQuery = true)
    List<UsersEntity> findByGroupId(Integer groupId);

    @Query(value = "Update users SET unique_id = ?1 WHERE id = ?2", nativeQuery = true)
    void linkUser(Integer uniqueId, Integer id);
}