package com.example.splitbill.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitbill.model.BillListDto
import com.example.splitbill.repository.local.BillShareRepository
import com.example.splitbill.repository.local.UserRepository
import com.example.splitbill.room_db.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val billShareRepo: BillShareRepository
) : ViewModel() {

    private var userList: LiveData<List<User>>? = null
    var userListState: MutableState<List<User>> = mutableStateOf(arrayListOf())

    fun getAllUsersByGroupId(groupId: Int) {
        viewModelScope.launch {
            val result = userRepo.getAllUsersByGroupId(groupId)
            withContext(Dispatchers.Main) {
                userList = result

                userList?.observeForever {
                    userListState.value = it
                }
            }
        }
    }


    fun addPeople(user: User) {
        viewModelScope.launch {
            userRepo.insert(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepo.deleteUser(user)
        }
    }


    var billListState = mutableStateOf(arrayListOf<List<BillListDto>?>())
    var groupId = 0

    fun getAllGroups(groupId: Int = 0) {
        if (groupId != 0)
            this.groupId = groupId

        viewModelScope.launch {
            val result = billShareRepo.getAllBills(this@UserListViewModel.groupId)
            withContext(Dispatchers.Main) {

                val res = result?.groupBy { model ->
                    model.billDetails?.bill_id
                }
                val billListStateTemp = arrayListOf<List<BillListDto>?>()
                res?.keys?.sortedBy { t -> t }?.forEach { id -> billListStateTemp.add(res[id]) }

                billListState.value = billListStateTemp
            }
        }
    }

}