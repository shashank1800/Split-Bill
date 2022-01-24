package com.shashankbhat.splitbill.repository.local

import androidx.compose.runtime.MutableState
import com.shashankbhat.splitbill.room_db.dao.UserDao
import com.shashankbhat.splitbill.room_db.entity.User
import com.shashankbhat.splitbill.util.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private var userDao: UserDao) {

    suspend fun insert(user: User?){
        userDao.insert(user)
    }

    suspend fun getAllUsersByGroupId(
        groupId: Int,
        userListState: MutableState<Response<List<User>>>
    ){
        val users = userDao.getAllUserByGroupId(groupId)
        userListState.value = Response.success(users)
    }

    suspend fun getAllUserByGroupId(groupId: Int): List<User>? {
        return userDao.getAllUserByGroupId(groupId)
    }

    suspend fun deleteUser(user: User?) {
        userDao.delete(user)
    }
}