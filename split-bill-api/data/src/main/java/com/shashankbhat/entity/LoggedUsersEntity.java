package com.shashankbhat.entity;

import com.common.util.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "logged_users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoggedUsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    String username;
    String password;
    Long dateCreated;

    public static Valid<LoggedUsersEntity> create(Integer id, String username, String password, Long dateCreated) {
        if (Objects.isNull(username))
            return Valid.fail("Username cannot be null");

        if (Objects.isNull(password))
            return Valid.fail("Password cannot be null");

        return Valid.success(new LoggedUsersEntity(id, username, password, dateCreated));
    }
}