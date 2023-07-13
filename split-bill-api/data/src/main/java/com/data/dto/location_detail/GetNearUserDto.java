package com.data.dto.location_detail;

/**
 * @author Shashank Bhat
 */

public interface GetNearUserDto {
    Integer getUniqueId();
    String getName();
    String getPhotoUrl();
    Double getLatitude();
    Double getLongitude();
}
