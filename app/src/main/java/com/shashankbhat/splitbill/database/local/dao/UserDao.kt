package com.shashankbhat.splitbill.database.local.dao

import androidx.room.*
import com.shashankbhat.splitbill.database.local.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User?)

    @Query("Update user SET id = :remoteId WHERE id = :localId")
    suspend fun update(localId: Int, remoteId: Int)

    @Delete
    suspend fun delete(delete: User?)

    @Query(
        "SELECT * FROM user " +
                "WHERE group_id= :groupId " +
                "ORDER BY date_created"
    )
    suspend fun getAllUserByGroupId(groupId: Int): List<User>?
}