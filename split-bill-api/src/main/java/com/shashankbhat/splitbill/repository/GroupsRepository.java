package com.shashankbhat.splitbill.repository;

import com.shashankbhat.splitbill.entity.GroupsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface GroupsRepository extends JpaRepository<GroupsEntity, Integer> {}
