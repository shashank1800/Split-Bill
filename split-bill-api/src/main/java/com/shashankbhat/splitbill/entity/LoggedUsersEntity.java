package com.shashankbhat.splitbill.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "logged_users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoggedUsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    String username;
    String password;
    Long dateCreated;
}