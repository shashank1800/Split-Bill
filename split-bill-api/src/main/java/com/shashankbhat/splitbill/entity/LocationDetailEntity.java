package com.shashankbhat.splitbill.entity;

import com.shashankbhat.splitbill.exception.ErrorMessage;
import com.shashankbhat.splitbill.util.Valid;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "location_detail")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LocationDetailEntity {
    @Id
    private Integer uniqueId;
    private Double distanceRange;
    private Double latitude;
    private Double longitude;
    private Long dateCreated;
    public static Valid<LocationDetailEntity> create(Integer uniqueId, Double distanceRange, Double latitude,
                                                     Double longitude, Long dateCreated) {
        if (Objects.isNull(uniqueId))
            return Valid.fail("UserId cannot be null");

        if (Objects.isNull(distanceRange))
            return Valid.fail(ErrorMessage.DISTANCE_RANGE_CANNOT_BE_EMPTY);

        return Valid.success(new LocationDetailEntity(uniqueId, distanceRange, latitude, longitude, dateCreated));
    }

}
