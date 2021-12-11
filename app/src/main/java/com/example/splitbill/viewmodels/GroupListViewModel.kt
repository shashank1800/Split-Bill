package com.example.splitbill.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.example.splitbill.model.Group
import com.example.splitbill.repository.local.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(private val repository: GroupRepository) : ViewModel() {

    private var groupsList : LiveData<List<Group>>? = null
    var groupsListState : MutableState<List<Group>> = mutableStateOf(arrayListOf())

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

    fun addGroup(group: Group) {
        viewModelScope.launch {
            repository.insert(group)
        }
    }


}