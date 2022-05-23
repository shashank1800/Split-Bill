package com.shashankbhat.splitbill.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_profile")
@Data
@AllArgsConstructor
public class UserProfileEntity {
    @Id
    private Integer uniqueId;
    private String name;
    private String photoUrl;
    private Boolean isNearbyVisible;
    private Long dateCreated;
}
