package com.shashankbhat.splitbill.viewmodels

import android.content.SharedPreferences
import android.os.CountDownTimer
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
    var isTakingMoreTime = mutableStateOf(false)

    fun getAllGroups() {
        isTakingMoreTime.value = false
        val timer = object: CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                isTakingMoreTime.value = true
            }
        }

        viewModelScope.launch {
            timer.start()
            groupRepoRemote.getAllGroups(groupsListState)
            withContext(Dispatchers.Main) {

                timer.cancel()
                isTakingMoreTime.value = false

                if (groupsListState.value.status == Status.Unauthorized)
                    unauthorized.value = true
            }
        }
    }

    fun addGroup(group: Groups) {

        GlobalScope.launch {
            groupRepository.getAllGroups(groupsListState)
            groupRepoRemote.insert(group){ type ->
                when(type.isLocal()){
                    type.isLocal() -> viewModelScope.launch {
                        groupRepository.getAllGroups(groupsListState)
                    }

                    type.isRemote() -> viewModelScope.launch {
                        groupRepoRemote.getAllGroups(groupsListState)
                    }
                }
            }
        }
    }

}