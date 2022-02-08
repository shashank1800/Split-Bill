package com.shashankbhat.splitbill.repository;

import com.shashankbhat.splitbill.entity.GroupsEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GroupsRepository extends JpaRepository<GroupsEntity, Integer> {
    List<GroupsEntity> findByUniqueId(Integer uniqueId/*, Sort sort*/);
}
