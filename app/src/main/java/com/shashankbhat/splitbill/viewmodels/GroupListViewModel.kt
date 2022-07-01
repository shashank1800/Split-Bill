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
import com.shashankbhat.splitbill.database.remote.repository.GroupRepositoryRemote
import com.shashankbhat.splitbill.database.remote.repository.LocationRepositoryRemote
import com.shashankbhat.splitbill.database.remote.repository.UserRepositoryRemote
import com.shashankbhat.splitbill.model.NearUserModel
import com.shashankbhat.splitbill.model.ProfileIconModel
import com.shashankbhat.splitbill.model.profile.DistanceRangeModel
import com.shashankbhat.splitbill.util.LatLong
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.Status
import com.shashankbhat.splitbill.util.extension.*
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
        mutableStateOf(Response.nothing())

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

    var addGroupResponse = MutableLiveData(Response.nothing<Int>())

    fun addGroupWithPeople(groupName: String) {

        val peopleList = nearUserList.value
            .filter { it.isSelected.get() }
            .map { it.uniqueId }
            .toList()

        viewModelScope.launch {

            val id = groupRepoRemote.insertWithPeople(groupName, peopleList)
            withContext(Dispatchers.Main) {
                if(id != null) {
                    nearUserList.value
                        .forEach {
                            it.isSelected.set(false)
                        }
                    groupRepository.getAllGroups(groupsListState)

                }

                addGroupResponse.value = Response.success(id)

            }
        }
    }

    // Profile
    var isNearbyEnabled = ObservableBoolean(sharedPreferences.getIsNearVisible())
    var isEditEnabled = ObservableBoolean(false)
    var distanceRange = ObservableField(DistanceRangeModel("${sharedPreferences.getDistanceRange().toInt()} KM", sharedPreferences.getDistanceRange()))
    var fullName = ObservableField(sharedPreferences.getFullName())
    var profilePhoto = ObservableField<ProfileIconModel>().also { profile ->
        sharedPreferences.getProfileIcons().forEachIndexed { index, it ->
            if(it == sharedPreferences.getPhotoUrl())
                profile.set(ProfileIconModel(index, it))
        }
    }

    var distanceList = arrayListOf<DistanceRangeModel>().apply {
        add(DistanceRangeModel("1 KM", 1.0))
        add(DistanceRangeModel("10 KM", 10.0))
        add(DistanceRangeModel("100 KM", 100.0))
        add(DistanceRangeModel("1000 KM", 1000.0))
    }

    var iconList = arrayListOf<ProfileIconModel>().also { list ->
        sharedPreferences.getProfileIcons().forEachIndexed { index, it ->
            list.add(ProfileIconModel(index, it))
        }
    }

    var nearUserList = MutableStateFlow(arrayListOf<NearUserModel>())

    fun getNearUser(location: LatLong) {

        sharedPreferences.setLocation(location)
        viewModelScope.launch {
            locationRepoRemote.getNearUser(location, nearUserList)
        }
    }

    fun saveProfile() {

        viewModelScope.launch {
            userRepoRemote.saveProfile(fullName.get(), profilePhoto.get()?.url, isNearbyEnabled.get(), distanceRange.get()?.distance)
        }
    }

    fun getProfile() {

        viewModelScope.launch {
            val response = userRepoRemote.getProfile()

            withContext(Dispatchers.Main) {
                fullName.set(sharedPreferences.getFullName())
                sharedPreferences.getProfileIcons().forEachIndexed { index, it ->
                    if(it == sharedPreferences.getPhotoUrl())
                        profilePhoto.set(ProfileIconModel(index, it))
                }
                isNearbyEnabled.set(response.data?.isNearbyVisible ?: false)
                distanceRange.set(DistanceRangeModel("${sharedPreferences.getDistanceRange().toInt()} KM", sharedPreferences.getDistanceRange()))
            }
        }
    }



}