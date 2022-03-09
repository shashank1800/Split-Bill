package com.shashankbhat.splitbill.viewmodels

import android.content.SharedPreferences
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.shashankbhat.splitbill.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.room_db.entity.Groups
import com.shashankbhat.splitbill.repository.remote.repository.GroupRepositoryRemote
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(
    private val groupRepoRemote: GroupRepositoryRemote,
    val sharedPreferences: SharedPreferences
) : ViewModel() {

    var groupsListState: MutableState<Response<List<GroupListDto>>> =
        mutableStateOf(Response.isNothing())

    var unauthorized = MutableLiveData(false)

    fun getAllGroups() {
        viewModelScope.launch {
            groupRepoRemote.getAllGroups(groupsListState)
            withContext(Dispatchers.Main) {
                if (groupsListState.value.status == Status.Unauthorized)
                    unauthorized.value = true
            }
        }
    }

    fun addGroup(group: Groups) {
        viewModelScope.launch {
            groupRepoRemote.insert(group)
            withContext(Dispatchers.Main) {
                getAllGroups()
            }
        }
    }

}