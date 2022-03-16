package com.shashankbhat.splitbill.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
) : ViewModel() {

    var userListState: MutableState<Response<List<User>>> = mutableStateOf(Response.isNothing())
    var groupId = 0
    var billList: MutableState<Response<List<BillModel>>> = mutableStateOf(Response.isNothing())

    fun getAllUsersByGroupId(groupId: Int = 0) {
        if (groupId != 0)
            this.groupId = groupId
        viewModelScope.launch {
            userRepoRemote.getAllUsersByGroupId(this@UserListViewModel.groupId, userListState)
        }
    }


    fun addPeople(user: User) {
        GlobalScope.launch {
            userRepoRemote.insert(user) { added ->
                if (added)
                    viewModelScope.launch {
                        userRepo.getAllUsersByGroupId(user.groupId, userListState)
                    }
            }
        }
    }

    fun deleteUser(user: User) {
        GlobalScope.launch {
            userRepoRemote.deleteUser(user){ deleted ->
                if (deleted)
                    viewModelScope.launch {
                        userRepo.getAllUsersByGroupId(user.groupId, userListState)
                    }
            }
        }
    }

    fun getAllBill(groupId: Int = 0) {
        if (groupId != 0)
            this.groupId = groupId

        viewModelScope.launch {
            billRepositoryRemote.getAllBill(this@UserListViewModel.groupId, billList)
        }

    }

}