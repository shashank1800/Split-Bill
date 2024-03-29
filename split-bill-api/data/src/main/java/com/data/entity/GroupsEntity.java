package com.data.entity;

import com.common.util.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "groups_tbl")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class GroupsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Integer id;
    String name;
    Long dateCreated;
    Integer uniqueId;

    public static Valid<GroupsEntity> create(Integer id, String name, Long dateCreated, Integer uniqueId) {
        if (Objects.isNull(uniqueId))
            return Valid.fail("Unique Id cannot be null");

        if (Objects.isNull(name) || name.isEmpty())
            return Valid.fail("Group name cannot be empty");

        return Valid.success(new GroupsEntity(id, name, dateCreated, uniqueId));
    }
}