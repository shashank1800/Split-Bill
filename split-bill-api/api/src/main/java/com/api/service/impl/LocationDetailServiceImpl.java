package com.api.service.impl;

import com.data.dto.location_detail.GetNearUserDto;
import com.common.exception.ErrorMessage;
import com.common.exception.KnownException;
import com.data.repository.LocationDetailRepository;
import com.data.repository.UserProfileRepository;
import com.api.dto.location_detail.NearUserListDto;
import com.api.service.ILocationDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public NearUserListDto getNearUsers(Integer uniqueId, Double latitude, Double longitude) throws KnownException {

        if(userProfileRepository.checkIsNearbyEnabled(uniqueId) == null)
            throw new KnownException(ErrorMessage.PLEASE_ENABLE_NEAR_VISIBILITY);

        locationDetailRepository.setLatLong(uniqueId, latitude, longitude);

        Double distanceRange = locationDetailRepository.getDistanceRange(uniqueId);
        double difference = distanceRange / (4.87 * 10);

        List<GetNearUserDto> nearUsers = locationDetailRepository.getNearUsers(latitude, longitude, difference, uniqueId);

        if(nearUsers.isEmpty())
            throw new KnownException("Oops!! Looks like there aren't any nearby users in your location.");

        return new NearUserListDto(nearUsers);
    }
}
