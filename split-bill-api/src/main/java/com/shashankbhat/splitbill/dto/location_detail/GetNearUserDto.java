package com.shashankbhat.splitbill.dto.location_detail;

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
