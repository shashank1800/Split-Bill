package com.api.splitbill.service;

import com.api.splitbill.dto.location_detail.NearUserListDto;

/**
 * @author Shashank Bhat
 */
public interface ILocationDetailService {
    NearUserListDto getNearUsers(Integer uniqueId, Double latitude, Double longitude) throws Exception;
}
