package com.shashankbhat.splitbill.ui.bill_shares.add_bill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.text.isDigitsOnly
import com.shashankbhat.splitbill.model.bill_shares.BillShareModel
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.database.local.entity.Bill
import com.shashankbhat.splitbill.ui.theme.SplitBillTheme
import com.shashankbhat.splitbill.ui.theme.Typography
import com.shashankbhat.splitbill.util.component.BottomWarningText
import com.shashankbhat.splitbill.viewmodels.BillShareViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shashankbhat.splitbill.ui.bill_shares.shares.BillShareUser

class AddBillSharesBottomSheetFragment(
    private val groupListDto: GroupListDto,
    private val viewModel: BillShareViewModel
) : BottomSheetDialogFragment() {

    private val billShareInputList = arrayListOf<BillShareModel>()

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        groupListDto.userList.forEach {
            billShareInputList.add(BillShareModel(it.id ?: -1))
        }

        return ComposeView(requireContext()).apply {
            setContent {
                SplitBillTheme {
                    CreateGroupCard()
                }
            }
        }
    }


    @ExperimentalMaterialApi
    @Composable
    fun CreateGroupCard() {

        var billName by remember {
            mutableStateOf("")
        }

        var isEmptyName by remember {
            mutableStateOf(false)
        }

        var totalAmount by remember {
            mutableStateOf("")
        }

        var isEmptyTotalAmount by remember {
            mutableStateOf(false)
        }

        var isAmountValid by remember {
            mutableStateOf(true)
        }

        ConstraintLayout(
            modifier = Modifier
                .padding(16.dp)
        ) {
            val (tvBillHead, tfBillName, tfTotalAmount, lcUsers, wtAmount, btnAdd, btnCancel) = createRefs()

            Text(
                text = "Add Bill",
                modifier = Modifier.constrainAs(tvBillHead) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
                style = Typography.h6
            )

            OutlinedTextField(
                value = billName,
                onValueChange = {
                    if (it.isNotEmpty())
                        isEmptyName = false
                    billName = it
                },
                modifier = Modifier
                    .constrainAs(tfBillName) {
                        top.linkTo(tvBillHead.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(tfTotalAmount.start)
                        width = Dimension.fillToConstraints
                    },
                label = {
                    Text(text = "Name")
                },
                isError = isEmptyName
            )

            OutlinedTextField(
                value = totalAmount,
                onValueChange = {
                    if (it.trim().isNotEmpty()){
                        isEmptyTotalAmount = false

                        billShareInputList.forEach { billShareModel ->
                            val equallySharedFloat = String.format("%.1f", (it.toFloat() / billShareInputList.size))
                            val equallySharedInt = String.format("%.0f", (it.toFloat() / billShareInputList.size))
                            var result = equallySharedFloat
                            if(equallySharedFloat.toFloat() == equallySharedInt.toFloat()) // removes unwanted zeros at the end
                                result = equallySharedInt
                            billShareModel.share.value = result
                        }
                    }

                    totalAmount = it
                },
                modifier = Modifier
                    .constrainAs(tfTotalAmount) {
                        top.linkTo(tvBillHead.bottom, margin = 16.dp)
                        start.linkTo(tfBillName.end, margin = 8.dp)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                label = {
                    Text(text = "Total Amount")
                },
                isError = isEmptyTotalAmount,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            LazyColumn(
                modifier = Modifier
                    .constrainAs(lcUsers) {
                        top.linkTo(tfBillName.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
            ) {
                itemsIndexed(groupListDto.userList) { index, user ->
                    BillShareUser(user = user, billShareModel = billShareInputList[index])
                }
            }

            if (!isAmountValid)
                BottomWarningText(
                    modifier = Modifier
                        .constrainAs(wtAmount) {
                            top.linkTo(lcUsers.bottom, margin = 8.dp)
                            start.linkTo(parent.start)
                        },
                    text = "Sum of spent and share should be always equal to total amount",
                    backgroundColor = Color(0xFFFF8B9C)
                )

            Button(
                modifier = Modifier.constrainAs(btnAdd) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
                onClick = {
                    if (billName.isEmpty()) {
                        isEmptyName = true
                    } else if (totalAmount.isEmpty() || !totalAmount.isDigitsOnly()) {
                        isEmptyTotalAmount = true
                    } else {

                        var sumSpent = 0F
                        var sumShare = 0F

                        billShareInputList.forEach {
                            sumSpent += it.spent.value.toFloat()
                            sumShare += it.share.value.toFloat()
                        }

                        isAmountValid = !(sumSpent != totalAmount.toFloat() || sumShare != totalAmount.toFloat())

                        if(isAmountValid){
                            viewModel.addBill(
                                Bill(
                                    groupListDto.group.id ?: -1,
                                    billName,
                                    totalAmount.toFloat()
                                ),
                                billShareInputList
                            )
                            dialog?.cancel()
                        }

                    }
                },
            ) {
                Text("Add")
            }

            OutlinedButton(
                onClick = { dialog?.cancel() },
                modifier = Modifier.constrainAs(btnCancel) {
                    top.linkTo(btnAdd.top)
                    bottom.linkTo(btnAdd.bottom)
                    end.linkTo(btnAdd.start, margin = 16.dp)
                }
            ) {
                Text("Cancel")
            }

        }

    }

}