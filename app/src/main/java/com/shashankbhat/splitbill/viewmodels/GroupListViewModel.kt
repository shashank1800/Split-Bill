package com.shashankbhat.splitbill.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.shashankbhat.splitbill.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.room_db.entity.Groups
import com.shashankbhat.splitbill.repository.local.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(private val repository: GroupRepository) : ViewModel() {

    private var groupsList : LiveData<List<GroupListDto>>? = null
    var groupsListState : MutableState<List<GroupListDto>> = mutableStateOf(arrayListOf())

    fun getAllGroups() {
        viewModelScope.launch {
            val result = repository.getAllGroups()
            withContext(Dispatchers.Main){
                groupsList = result

                groupsList?.observeForever {
                    groupsListState.value = it
                }
            }
        }
    }

    fun addGroup(group: Groups) {
        viewModelScope.launch {
            repository.insert(group)
        }
    }

}