package com.shashankbhat.splitbill.repository.local

import androidx.lifecycle.LiveData
import com.shashankbhat.splitbill.room_db.dao.UserDao
import com.shashankbhat.splitbill.room_db.entity.User
import javax.inject.Inject

class UserRepository @Inject constructor(private var userDao: UserDao) {

    suspend fun insert(user: User?){
        userDao.insert(user)
    }

    suspend fun getAllUsersByGroupId(groupId: Int): LiveData<List<User>>? {
        return userDao.getAllUserByGroupId(groupId)
    }

    suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }
}