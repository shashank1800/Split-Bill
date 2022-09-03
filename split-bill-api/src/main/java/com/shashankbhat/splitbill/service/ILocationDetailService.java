package com.shashankbhat.splitbill.service;

import com.shashankbhat.splitbill.dto.location_detail.NearUserListDto;

import java.util.List;

/**
 * @author Shashank Bhat
 */
public interface ILocationDetailService {
    NearUserListDto getNearUsers(Integer uniqueId, Double latitude, Double longitude) throws Exception;
}
