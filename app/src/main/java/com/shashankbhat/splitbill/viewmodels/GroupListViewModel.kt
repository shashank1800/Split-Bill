package com.shashankbhat.splitbill.viewmodels

import android.content.SharedPreferences
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.database.local.entity.Groups
import com.shashankbhat.splitbill.database.local.repository.GroupRepository
import com.shashankbhat.splitbill.database.remote.repository.GroupRepositoryRemote
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(
    private val groupRepoRemote: GroupRepositoryRemote,
    private val groupRepository: GroupRepository,
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

        GlobalScope.launch {
            groupRepository.getAllGroups(groupsListState)
            groupRepoRemote.insert(group){added ->
                if(added)
                    viewModelScope.launch {
                        groupRepository.getAllGroups(groupsListState)
                    }
            }
        }
    }

}