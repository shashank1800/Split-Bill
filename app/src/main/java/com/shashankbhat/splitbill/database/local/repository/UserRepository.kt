package com.shashankbhat.splitbill.database.local.repository

import androidx.lifecycle.MutableLiveData
import com.shashankbhat.splitbill.database.local.dao.UserDao
import com.shashankbhat.splitbill.database.local.entity.User
import com.shashankbhat.splitbill.util.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private var userDao: UserDao) {

    suspend fun insert(user: User?){
        userDao.insert(user)
    }

    suspend fun update(localId: Int, remoteId: Int) {
        userDao.update(localId, remoteId)
    }

    suspend fun getAllUsersByGroupId(
        groupId: Int,
        userListState: MutableLiveData<Response<List<User>>>? = null
    ): List<User>?{

        if(userListState == null)
            return userDao.getAllUserByGroupId(groupId)

        userListState.let {
            val users = userDao.getAllUserByGroupId(groupId)
            userListState.value = Response.success(users)
        }

        return null
    }

    suspend fun deleteUser(user: User?) {
        userDao.delete(user)
    }
}