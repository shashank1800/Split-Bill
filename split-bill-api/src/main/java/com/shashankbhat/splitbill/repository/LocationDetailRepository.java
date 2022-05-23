package com.shashankbhat.splitbill.repository;


import com.shashankbhat.splitbill.entity.LocationDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationDetailRepository extends JpaRepository<LocationDetailEntity, Integer> {

    @Modifying
    @Query(value = "UPDATE location_detail SET latitude = ?2 longitude = ?3 WHERE unique_id = ?1", nativeQuery = true)
    void setLatLong(Integer uniqueId, Double latitude, Double longitude);

    @Modifying
    @Query(value = "UPDATE location_detail SET distance_range = ?2 WHERE unique_id = ?1", nativeQuery = true)
    void updateLocationRange(Integer uniqueId, Double distanceRange);

}