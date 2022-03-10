package com.shashankbhat.splitbill.repository;

import com.shashankbhat.splitbill.entity.GroupsEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GroupsRepository extends JpaRepository<GroupsEntity, Integer> {
    List<GroupsEntity> findByUniqueId(Integer uniqueId/*, Sort sort*/);
//    @Query(value = "SELECT * FROM groups_tbl WHERE unique_id = ?1 " +
//            "UNION " +
//            "SELECT * FROM groups_tbl g WHERE g.id IN (SELECT DISTINCT u.group_id FROM users u WHERE unique_id = ?1) " +
//            "ORDER BY date_created DESC;", nativeQuery = true)
//    List<GroupsEntity> findAllGroupsWithUniqueId(Integer uniqueId);
}
