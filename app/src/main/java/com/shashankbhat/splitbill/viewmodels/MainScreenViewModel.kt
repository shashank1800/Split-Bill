package com.shashankbhat.splitbill.viewmodels

import android.content.SharedPreferences
import android.os.CountDownTimer
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.viewpager2.widget.ViewPager2
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupRecyclerListDto
import com.shashankbhat.splitbill.database.local.entity.Groups
import com.shashankbhat.splitbill.database.local.repository.GroupRepository
import com.shashankbhat.splitbill.database.remote.repository.GroupRepositoryRemote
import com.shashankbhat.splitbill.database.remote.repository.LocationRepositoryRemote
import com.shashankbhat.splitbill.database.remote.repository.UserRepositoryRemote
import com.shashankbhat.splitbill.database.local.model.NearUserModel
import com.shashankbhat.splitbill.database.local.model.ProfileIconModel
import com.shashankbhat.splitbill.database.local.model.profile.DistanceRangeModel
import com.shashankbhat.splitbill.util.LatLong
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.extension.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val groupRepoRemote: GroupRepositoryRemote,
    private val groupRepository: GroupRepository,
    val sharedPreferences: SharedPreferences,
    private val locationRepoRemote: LocationRepositoryRemote,
    private val userRepoRemote: UserRepositoryRemote,
) : ViewModel() {

    var groupsListState: MutableLiveData<Response<List<GroupRecyclerListDto>>> =
        MutableLiveData(Response.nothing())
    var isGroupListEmpty = ObservableBoolean(false)
    var isRefreshing = ObservableBoolean(false)
    var unauthorized = MutableLiveData(false)
    var isTakingMoreTime = MutableLiveData(false)
    var vpBillShares: ViewPager2? = null

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

                if (groupsListState.value?.isUnauthorized() == true) {
                    sharedPreferences.putToken("")
                    unauthorized.value = true
                }

                isRefreshing.set(false)
            }
        }
    }

    fun addGroup(group: Groups) {

        GlobalScope.launch {
            groupRepoRemote.insert(group) { type ->
                when {
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

        val peopleList = nearUserList.value?.data
            ?.filter { it.isSelected.get() }
            ?.map { it.uniqueId }
            ?.toList()

        viewModelScope.launch {

            val id = groupRepoRemote.insertWithPeople(groupName, peopleList)
            withContext(Dispatchers.Main) {
                if(id != null) {
                    nearUserList.value?.data
                        ?.forEach {
                            it.isSelected.set(false)
                        }
                }

                addGroupResponse.value = Response.success(id)

            }
        }
    }

    // Profile
    var isNearbyEnabled = ObservableBoolean(sharedPreferences.getIsNearVisible())
    var hasProfileData = ObservableBoolean(sharedPreferences.getFullName().isNotEmpty())
    var distanceRange = ObservableField(DistanceRangeModel("${sharedPreferences.getDistanceRange().toInt()} KM", sharedPreferences.getDistanceRange()))
    var fullName = ObservableField(sharedPreferences.getFullName())
    var profilePhoto = ObservableField<ProfileIconModel>().also { profile ->
        getProfileIcons().forEachIndexed { index, it ->
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
        getProfileIcons().forEachIndexed { index, it ->
            list.add(ProfileIconModel(index, it))
        }
    }

    var nearUserList = MutableLiveData<Response<ArrayList<NearUserModel>>>(Response.nothing())
    var isNearbyPeopleEmpty = ObservableBoolean(true)
    fun getNearUser(location: LatLong) {

        sharedPreferences.setLocation(location)
        viewModelScope.launch {
            locationRepoRemote.getNearUser(location, nearUserList)
            withContext(Dispatchers.IO){
                isRefreshing.set(false)
            }
        }
    }

    fun saveProfile() {

        viewModelScope.launch {
            val response = userRepoRemote.saveProfile(fullName.get(), profilePhoto.get()?.url, isNearbyEnabled.get(), distanceRange.get()?.distance)
            withContext(Dispatchers.IO){
                when{
                    response.isSuccess() -> hasProfileData.set(true)
                }
            }
        }
    }

    fun getProfile() {

        viewModelScope.launch {
            val response = userRepoRemote.getProfile()

            withContext(Dispatchers.Main) {
                when{
                    response.isSuccess() -> {
                        hasProfileData.set(true)
                        fullName.set(sharedPreferences.getFullName())
                        getProfileIcons().forEachIndexed { index, it ->
                            if(it == sharedPreferences.getPhotoUrl())
                                profilePhoto.set(ProfileIconModel(index, it))
                        }
                        isNearbyEnabled.set(response.data?.isNearbyVisible ?: sharedPreferences.getIsNearVisible())
                        distanceRange.set(DistanceRangeModel("${sharedPreferences.getDistanceRange().toInt()} KM", sharedPreferences.getDistanceRange()))
                    }
                }

            }
        }
    }

    var updateProfilePhotoResponse: MutableLiveData<Response<String>> = MutableLiveData(Response.nothing())

    fun updateProfilePhoto(profileIconModel: ProfileIconModel) {
        viewModelScope.launch {
            val response = userRepoRemote.updateProfilePhoto(profileIconModel.url)
            withContext(Dispatchers.Main) {
                if(response.isSuccess()){
                    updateProfilePhotoResponse.value = response
                    profilePhoto.set(profileIconModel)
                }
            }
        }
    }


}