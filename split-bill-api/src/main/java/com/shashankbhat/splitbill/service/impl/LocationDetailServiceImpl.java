package com.shashankbhat.splitbill.service.impl;

import com.shashankbhat.splitbill.dto.location_detail.GetNearUserDto;
import com.shashankbhat.splitbill.exception.ErrorMessage;
import com.shashankbhat.splitbill.exception.KnownException;
import com.shashankbhat.splitbill.repository.LocationDetailRepository;
import com.shashankbhat.splitbill.repository.UserProfileRepository;
import com.shashankbhat.splitbill.service.ILocationDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Shashank Bhat
 */
@Service
public class LocationDetailServiceImpl implements ILocationDetailService {

    @Autowired
    private LocationDetailRepository locationDetailRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public List<GetNearUserDto> getNearUsers(Integer uniqueId, Double latitude, Double longitude) throws Exception {

        // TODO : save latest location

        if(!userProfileRepository.checkIsNearbyEnabled(uniqueId))
            throw new KnownException(ErrorMessage.PLEASE_ENABLE_NEAR_VISIBILITY);

        Double distanceRange = locationDetailRepository.getDistanceRange(uniqueId);
        double difference = distanceRange / (4.87 * 10);

        return locationDetailRepository.getNearUsers(latitude, longitude, difference);
    }
}
