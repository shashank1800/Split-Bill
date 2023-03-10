package com.shashankbhat.entity;

import com.shashankbhat.util.Valid;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "logged_users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@Getter
public class LoggedUsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    String username;
    String password;
    Long dateCreated;

    private LoggedUsersEntity(Integer id, String username, String password, Long dateCreated) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.dateCreated = dateCreated;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public static Valid<LoggedUsersEntity> create(Integer id, String username, String password, Long dateCreated) {
        if (Objects.isNull(username))
            return Valid.fail("Username cannot be null");

        if (Objects.isNull(password))
            return Valid.fail("Password cannot be null");

        return Valid.success(new LoggedUsersEntity(id, username, password, dateCreated));
    }
}