package com.shashankbhat.splitbill.ui

object ApiConstants {

//    const val BASE_URL = "http://192.168.1.6:8080/"
//    const val BASE_URL = "http://192.168.95.121:8080/"

    const val AUTHORIZATION = "Authorization"

    const val saveGroup = "group/saveGroup"
    const val allGroups = "group/allGroups"

    const val saveUser = "users/saveUser"
    const val getAllUser = "users/getAllUser"
    const val deleteUser = "users/deleteUser"
    const val linkUser = "users/linkUser"

    const val saveBill = "bill/saveBill"
    const val getAllBill = "bill/getBills"
    const val deleteBill = "bill/deleteBills"

    const val saveProfile = "user_profile/saveProfile"
    const val locationPreference = "user_profile/locationPreference"
    const val updateName = "user_profile/updateName"
    const val profileDetail = "user_profile/profileDetail"
    const val updateProfilePhoto = "user_profile/updateProfilePhoto"

    const val updateLocationRange = "location_detail/updateLocationRange"
    const val getNearUsers = "location_detail/getNearUsers"

    const val authentication = "authenticate"
}