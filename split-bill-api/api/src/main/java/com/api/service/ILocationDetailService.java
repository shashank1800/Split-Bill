package com.api.service;

import com.api.dto.location_detail.NearUserListDto;

/**
 * @author Shashank Bhat
 */
public interface ILocationDetailService {
    NearUserListDto getNearUsers(Integer uniqueId, Double latitude, Double longitude) throws Exception;
}
