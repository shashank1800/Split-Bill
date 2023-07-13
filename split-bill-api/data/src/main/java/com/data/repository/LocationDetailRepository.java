package com.data.repository;


import com.data.dto.location_detail.GetNearUserDto;
import com.data.entity.LocationDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationDetailRepository extends JpaRepository<LocationDetailEntity, Integer> {

    @Modifying
    @Query(value = "UPDATE location_detail SET latitude = ?2, longitude = ?3 WHERE unique_id = ?1", nativeQuery = true)
    void setLatLong(Integer uniqueId, Double latitude, Double longitude);

    @Modifying
    @Query(value = "UPDATE location_detail SET distance_range = ?2 WHERE unique_id = ?1", nativeQuery = true)
    void updateLocationRange(Integer uniqueId, Double distanceRange);

    @Query(value = "SELECT up.unique_id as uniqueId, up.name, up.photo_url as photoUrl, ld.latitude, ld.longitude from user_profile up " +
            "INNER JOIN location_detail ld on ld.unique_id = up.unique_id " +
            "WHERE ld.latitude < (?1 + ?3) and ld.longitude < (?2 + ?3) " +
            "and ld.latitude > (?1 - ?3) and ld.longitude > (?2 - ?3) " +
            "and up.is_nearby_visible = true AND up.unique_id <> ?4 ", nativeQuery = true)
    List<GetNearUserDto> getNearUsers(Double latitude, Double longitude, Double difference, Integer uniqueId);


    @Query(value = "SELECT ld.distance_range FROM location_detail ld " +
            "WHERE unique_id = ?1", nativeQuery = true)
    Double getDistanceRange(Integer uniqueId);

    LocationDetailEntity findOneByUniqueId(Integer uniqueId);
}