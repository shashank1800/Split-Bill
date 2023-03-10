package com.shashankbhat.entity;

import com.shashankbhat.util.Valid;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    Integer groupId;
    String name;
    Long dateCreated;
    Integer uniqueId;

    protected UsersEntity(Integer id, Integer groupId, String name, Long dateCreated, Integer uniqueId) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
        this.dateCreated = dateCreated;
        this.uniqueId = uniqueId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Integer uniqueId) {
        this.uniqueId = uniqueId;
    }

    public static Valid<UsersEntity> create(Integer id, Integer groupId, String name, Long dateCreated, Integer uniqueId) {
        if (Objects.isNull(uniqueId))
            return Valid.fail("Unique Id cannot be null");

        if (Objects.isNull(groupId))
            return Valid.fail("Group Id cannot be null");

        if (Objects.isNull(name) || name.isEmpty())
            return Valid.fail("Bil name cannot be empty");

        return Valid.success(new UsersEntity(id, groupId, name, dateCreated, uniqueId));
    }

}