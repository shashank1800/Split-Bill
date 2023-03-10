package com.shashankbhat.entity;

import com.shashankbhat.exception.ErrorMessage;
import com.shashankbhat.util.Valid;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "location_detail")
@Getter
public class LocationDetailEntity {
    @Id
    private Integer uniqueId;
    private Double distanceRange;
    private Double latitude;
    private Double longitude;
    private Long dateCreated;


    protected LocationDetailEntity(Integer uniqueId, Double distanceRange, Double latitude, Double longitude,
                           Long dateCreated) {
        this.uniqueId = uniqueId;
        this.distanceRange = distanceRange;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateCreated = dateCreated;
    }

    public static Valid<LocationDetailEntity> create(Integer uniqueId, Double distanceRange, Double latitude,
                                                     Double longitude, Long dateCreated) {
        if (Objects.isNull(uniqueId))
            return Valid.fail("UserId cannot be null");

        if (Objects.isNull(distanceRange))
            return Valid.fail(ErrorMessage.DISTANCE_RANGE_CANNOT_BE_EMPTY);

        return Valid.success(new LocationDetailEntity(uniqueId, distanceRange, latitude, longitude, dateCreated));
    }

}
