package com.shashankbhat.splitbill.entity;

import com.shashankbhat.splitbill.exception.ErrorMessage;
import com.shashankbhat.splitbill.util.Valid;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "user_profile")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

        return Valid.success(new UserProfileEntity(uniqueId, name, photoUrl, isNearbyVisible, dateCreated));
    }

}
