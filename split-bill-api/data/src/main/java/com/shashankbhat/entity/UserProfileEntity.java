package com.shashankbhat.entity;


import com.common.exception.ErrorMessage;
import com.common.util.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "user_profile")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserProfileEntity {
    @Id
    private Integer uniqueId;
    private String name;
    private String photoUrl;
    private Boolean isNearbyVisible;
    private Long dateCreated;

    public static Valid<UserProfileEntity> create(Integer uniqueId, String name, String photoUrl,
                                                  Boolean isNearbyVisible, Long dateCreated) {
        if (Objects.isNull(uniqueId))
            return Valid.fail("UserId cannot be null");

        if (Objects.isNull(name) || name.isEmpty())
            return Valid.fail(ErrorMessage.USER_NAME_CANNOT_BE_EMPTY);

        if (Objects.isNull(photoUrl) || photoUrl.isEmpty())
            return Valid.fail(ErrorMessage.AVATAR_CANNOT_BE_EMPTY);

        if (Objects.isNull(isNearbyVisible))
            return Valid.fail(ErrorMessage.IS_NEAR_VISIBLE_CANNOT_BE_EMPTY);

        UserProfileEntity userProfileEntity = new UserProfileEntity(uniqueId, name, photoUrl, isNearbyVisible,
                dateCreated);
        return Valid.success(userProfileEntity);
    }

}
