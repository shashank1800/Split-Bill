package com.shashankbhat.splitbill.dto.location_detail;

import lombok.Data;

/**
 * @author Shashank Bhat
 */
@Data
public class GetNearUserDto {
    private Integer uniqueId;
    private String name;
    private String photoUrl;
    private Double latitude;
    private Double longitude;
}
