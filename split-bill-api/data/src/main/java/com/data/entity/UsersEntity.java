package com.data.entity;

import com.common.util.Valid;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    @Setter
    Integer groupId;
    String name;
    Long dateCreated;
    Integer uniqueId;

    public static Valid<UsersEntity> create(Integer id, Integer groupId, String name, Long dateCreated, Integer uniqueId) {

        if (Objects.isNull(groupId))
            return Valid.fail("Group Id cannot be null");

        if (Objects.isNull(name) || name.isEmpty())
            return Valid.fail("Bil name cannot be empty");

        return Valid.success(new UsersEntity(id, groupId, name, dateCreated, uniqueId));
    }

}