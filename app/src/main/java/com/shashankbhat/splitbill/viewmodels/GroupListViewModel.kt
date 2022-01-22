package com.shashankbhat.splitbill.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.shashankbhat.splitbill.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.room_db.entity.Groups
import com.shashankbhat.splitbill.repository.local.GroupRepository
import com.shashankbhat.splitbill.repository.remote.repository.GroupRepositoryRemote
import com.shashankbhat.splitbill.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(private val groupRepoLocal: GroupRepository, private val groupRepoRemote: GroupRepositoryRemote) : ViewModel() {

    var groupsListState : MutableState<Response<List<GroupListDto>>> = mutableStateOf(Response.isNothing())

    fun getAllGroups() {
        groupsListState.value = Response.loading()
        viewModelScope.launch {
            groupRepoRemote.getAllGroups(groupsListState)
        }
    }

    fun addGroup(group: Groups) {
        viewModelScope.launch {
            groupRepoRemote.insert(group)
            withContext(Dispatchers.Main){
                getAllGroups()
            }
        }
    }

}