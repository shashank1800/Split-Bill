package com.shashankbhat.splitbill.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashankbhat.splitbill.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.dto.bill_shares.BillSharesModel
import com.shashankbhat.splitbill.repository.local.BillRepository
import com.shashankbhat.splitbill.repository.local.BillShareRepository
import com.shashankbhat.splitbill.repository.local.UserRepository
import com.shashankbhat.splitbill.room_db.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val billShareRepo: BillShareRepository,
    private val billRepo: BillRepository
) : ViewModel() {

    private var userList: LiveData<List<User>>? = null
    var userListState: MutableState<List<User>> = mutableStateOf(arrayListOf())

    fun getAllUsersByGroupId(groupId: Int) {
        viewModelScope.launch {
            val result = userRepo.getAllUsersByGroupId(groupId)
            withContext(Dispatchers.Main) {
                userList = result

                userList?.observeForever {
                    userListState.value = it
                }
            }
        }
    }


    fun addPeople(user: User) {
        viewModelScope.launch {
            userRepo.insert(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepo.deleteUser(user)
        }
    }

    var groupId = 0
    var billList = mutableStateOf(arrayListOf<BillModel>())

    fun getAllBill(groupId: Int = 0) {
        if (groupId != 0)
            this.groupId = groupId

        viewModelScope.launch {

            val bills = arrayListOf<BillModel>()

            val allBill = async { billRepo.getAllBill(this@UserListViewModel.groupId) }
            val allUsersByGroup =
                async { userRepo.getAllUserByGroupId(this@UserListViewModel.groupId) }
            val userToIdMap = hashMapOf<Int, User>()

            allUsersByGroup.await().forEach { user ->
                userToIdMap[user.id] = user
            }

            allBill.await().forEach { bill ->
                val billModel = BillModel(
                    bill.id,
                    bill.group_id,
                    bill.name,
                    bill.total_amount,
                    bill.date_created,
                    null
                )
                bills.add(billModel)

                val allBillShares = async { billShareRepo.getBillShareByBillId(bill.id) }
                val billShares = arrayListOf<BillSharesModel>()

                allBillShares.await().forEachIndexed { billShareIndex, billShare ->
                    billShares.add(
                        BillSharesModel(
                            billShare.id,
                            billShare.bill_id,
                            billShare.user_id,
                            billShare.spent,
                            billShare.share,
                            billShare.date_created,
                            userToIdMap.get(billShare.user_id)
                        )
                    )
                }

                billModel.billShares = billShares
            }

            billList.value = bills
        }
    }

}