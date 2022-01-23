package com.shashankbhat.splitbill.room_db.dao

import androidx.room.*
import com.shashankbhat.splitbill.room_db.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User?)

    @Delete
    suspend fun delete(delete: User?)

    @Query(
        "SELECT * FROM user " +
                "WHERE group_id= :groupId " +
                "ORDER BY date_created"
    )
    suspend fun getAllUserByGroupId(groupId: Int): List<User>?
}