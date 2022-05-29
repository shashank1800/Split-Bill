package com.shashankbhat.splitbill.viewmodels

import android.content.SharedPreferences
import android.os.CountDownTimer
import androidx.compose.runtime.*
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.database.local.entity.Groups
import com.shashankbhat.splitbill.database.local.repository.GroupRepository
import com.shashankbhat.splitbill.database.remote.entity.NearUsersList
import com.shashankbhat.splitbill.database.remote.repository.GroupRepositoryRemote
import com.shashankbhat.splitbill.database.remote.repository.LocationRepositoryRemote
import com.shashankbhat.splitbill.database.remote.repository.UserRepositoryRemote
import com.shashankbhat.splitbill.model.profile.DistanceRangeModel
import com.shashankbhat.splitbill.util.LatLong
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.Status
import com.shashankbhat.splitbill.util.extension.putToken
import com.shashankbhat.splitbill.util.extension.setLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(
    private val groupRepoRemote: GroupRepositoryRemote,
    private val groupRepository: GroupRepository,
    val sharedPreferences: SharedPreferences,
    private val locationRepoRemote: LocationRepositoryRemote,
    private val userRepoRemote: UserRepositoryRemote,
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

    // Profile
    var isNearbyEnabled = ObservableBoolean(false)
    var isEditEnabled = ObservableBoolean(false)
    var distanceRange = ObservableField<DistanceRangeModel>()

    var distanceList = arrayListOf<DistanceRangeModel>().apply {
        add(DistanceRangeModel("1 KM", 1.0))
        add(DistanceRangeModel("10 KM", 10.0))
        add(DistanceRangeModel("100 KM", 100.0))
        add(DistanceRangeModel("1000 KM", 1000.0))
    }

    var nearUserList = MutableStateFlow(NearUsersList(arrayListOf()))

    fun getNearUser(location: LatLong) {

        sharedPreferences.setLocation(location)
        viewModelScope.launch {
            locationRepoRemote.getNearUser(location, nearUserList)
        }
    }

    fun saveProfile(name: String) {

        viewModelScope.launch {
            userRepoRemote.saveProfile(name, "https://lh3.googleusercontent.com/ogw/ADea4I6FVPUszdCfkCSoIP6ccUa87r1YFt7TMU3JhuVssA=s32-c-mo", isNearbyEnabled.get(), distanceRange.get()?.distance)
        }
    }



}