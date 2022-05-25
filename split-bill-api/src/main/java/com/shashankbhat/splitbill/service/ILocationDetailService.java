package com.shashankbhat.splitbill.service;

import com.shashankbhat.splitbill.dto.location_detail.GetNearUserDto;

import java.util.List;

/**
 * @author Shashank Bhat
 */
public interface ILocationDetailService {
    List<GetNearUserDto> getNearUsers(Integer uniqueId, Double latitude, Double longitude) throws Exception;
}
