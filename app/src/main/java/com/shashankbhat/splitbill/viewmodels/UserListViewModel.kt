package com.shashankbhat.splitbill.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashankbhat.splitbill.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.dto.bill_shares.BillSharesModel
import com.shashankbhat.splitbill.repository.local.BillRepository
import com.shashankbhat.splitbill.repository.local.BillShareRepository
import com.shashankbhat.splitbill.repository.local.UserRepository
import com.shashankbhat.splitbill.repository.remote.repository.UserRepositoryRemote
import com.shashankbhat.splitbill.room_db.entity.User
import com.shashankbhat.splitbill.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepoRemote: UserRepositoryRemote,
    private val userRepo: UserRepository,
    private val billShareRepo: BillShareRepository,
    private val billRepo: BillRepository
) : ViewModel() {

    var userListState: MutableState<Response<List<User>>> = mutableStateOf(Response.isNothing())
    var groupId = 0
    var billList = mutableStateOf(arrayListOf<BillModel>())

    fun getAllUsersByGroupId(groupId: Int = 0) {
        if (groupId != 0)
            this.groupId = groupId
        viewModelScope.launch {
            userRepoRemote.getAllUsersByGroupId(this@UserListViewModel.groupId, userListState)
        }
    }


    fun addPeople(user: User) {
        viewModelScope.launch {
            userRepoRemote.insert(user)
            withContext(Dispatchers.Main){
                getAllUsersByGroupId()
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepoRemote.deleteUser(user)
            withContext(Dispatchers.Main){
                getAllUsersByGroupId()
            }
        }
    }

    fun getAllBill(groupId: Int = 0) {
        if (groupId != 0)
            this.groupId = groupId

        viewModelScope.launch {

            val bills = arrayListOf<BillModel>()

            val allBill = async { billRepo.getAllBill(this@UserListViewModel.groupId) }
            val allUsersByGroup =
                async { userRepo.getAllUserByGroupId(this@UserListViewModel.groupId) }
            val userToIdMap = hashMapOf<Int, User>()

            allUsersByGroup.await()?.forEach { user ->
                userToIdMap[user.id ?: -1] = user
            }

            allBill.await().forEach { bill ->
                val billModel = BillModel(
                    bill.id,
                    bill.groupId,
                    bill.name,
                    bill.totalAmount,
                    bill.dateCreated,
                    null
                )
                bills.add(billModel)

                val allBillShares = async { billShareRepo.getBillShareByBillId(bill.id ?: -1) }
                val billShares = arrayListOf<BillSharesModel>()

                allBillShares.await().forEachIndexed { billShareIndex, billShare ->
                    billShares.add(
                        BillSharesModel(
                            billShare.id,
                            billShare.billId,
                            billShare.userId,
                            billShare.spent,
                            billShare.share,
                            billShare.dateCreated,
                            userToIdMap.get(billShare.userId)
                        )
                    )
                }

                billModel.billShares = billShares
            }

            billList.value = bills
        }
    }

}