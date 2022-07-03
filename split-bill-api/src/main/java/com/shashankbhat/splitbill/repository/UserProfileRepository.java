package com.shashankbhat.splitbill.repository;

import com.shashankbhat.splitbill.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Integer> {

    @Modifying
    @Query(value = "UPDATE user_profile SET is_nearby_visible = ?1 WHERE unique_id = ?2", nativeQuery = true)
    void locationPreference(Boolean isNearbyVisible, Integer uniqueId);

    @Modifying
    @Query(value = "UPDATE user_profile SET name = ?2 WHERE unique_id = ?1", nativeQuery = true)
    void updateName(Integer uniqueId, String name);

    @Query(value = "SELECT up.unique_id FROM user_profile up " +
            "WHERE unique_id = ?1 AND up.is_nearby_visible = true ", nativeQuery = true)
    Integer checkIsNearbyEnabled(Integer uniqueId);


    @Modifying
    @Query(value = "UPDATE user_profile SET photo_url = ?2 WHERE unique_id = ?1", nativeQuery = true)
    void updateProfilePhoto(Integer uniqueId, String photoUrl);
}
