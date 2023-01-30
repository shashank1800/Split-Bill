package com.shashankbhat.splitbill.viewmodels

import android.content.SharedPreferences
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashankbhat.splitbill.database.local.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.database.remote.repository.BillRepositoryRemote
import com.shashankbhat.splitbill.database.remote.repository.UserRepositoryRemote
import com.shashankbhat.splitbill.database.local.entity.User
import com.shashankbhat.splitbill.database.local.repository.UserRepository
import com.shashankbhat.splitbill.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepoRemote: UserRepositoryRemote,
    private val billRepositoryRemote: BillRepositoryRemote,
    private val userRepo: UserRepository,
    val sharedPreferences: SharedPreferences
) : ViewModel() {

    var userListState: MutableLiveData<Response<List<User>>> = MutableLiveData(Response.nothing())
    var groupId = 0
    var billList: MutableLiveData<Response<List<BillModel>>> = MutableLiveData(Response.nothing())
    var isBillListEmpty = ObservableBoolean(false)

    fun getAllUsersByGroupId(groupId: Int = 0) {
        if (groupId != 0)
            this.groupId = groupId
        viewModelScope.launch {
            userRepoRemote.getAllUsersByGroupId(this@UserListViewModel.groupId, userListState)
        }
    }


    fun addPeople(user: User) {
        GlobalScope.launch {
            userRepoRemote.insert(user) { type ->
                when {
                    type.isLocal() -> viewModelScope.launch {
                        userRepo.getAllUsersByGroupId(user.groupId, userListState)
                    }

                    type.isRemote() -> viewModelScope.launch {
                        userRepoRemote.getAllUsersByGroupId(user.groupId, userListState)
                    }
                }
            }
        }
    }

    fun deleteUser(user: User?) {
        user?.let {
            GlobalScope.launch {
                userRepoRemote.deleteUser(user){ type ->

                    when {
                        type.isLocal() -> viewModelScope.launch {
                            userRepo.getAllUsersByGroupId(user.groupId, userListState)
                        }
                    }
                }
            }
        }
    }

    fun getAllBill(groupId: Int = 0) {
        if (groupId != 0)
            this.groupId = groupId

        viewModelScope.launch {
            billRepositoryRemote.getAllBill(this@UserListViewModel.groupId, billList)
            withContext(Dispatchers.Main){
                billList.value?.data?.isEmpty()?.let { isBillListEmpty.set(it) }
            }
        }

    }

    fun linkPeople(user: User?, uniqueId: String?) {
        GlobalScope.launch {
            userRepoRemote.linkUser(user?.id, uniqueId) { type ->
                when {
                    type.isLocal() -> viewModelScope.launch {
                        userRepo.getAllUsersByGroupId(user?.groupId ?: -1, userListState)
                    }

                    type.isRemote() -> viewModelScope.launch {
                        userRepoRemote.getAllUsersByGroupId(user?.groupId ?: -1, userListState)
                    }
                }
            }
        }
    }

}