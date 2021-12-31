package com.example.splitbill.ui.bill_shares

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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.splitbill.model.BillShareModel
import com.example.splitbill.model.GroupListModel
import com.example.splitbill.room_db.entity.Bill
import com.example.splitbill.ui.theme.SplitBillTheme
import com.example.splitbill.ui.theme.Typography
import com.example.splitbill.viewmodels.BillShareViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddBillSharesFragment(private val groupListModel:GroupListModel, private val viewModel: BillShareViewModel, private val navController: NavController) : BottomSheetDialogFragment() {

    private val billShareInputList = arrayListOf<BillShareModel>()

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        groupListModel.userList.forEach {
            billShareInputList.add(BillShareModel(it.id))
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
    @ExperimentalComposeUiApi
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


        ConstraintLayout(
            modifier = Modifier
                .padding(16.dp)
        ) {
            val (tvBillHead, tfBillName, tfTotalAmount, lcUsers, btnAdd, btnCancel) = createRefs()

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
                    if(it.isNotEmpty())
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
                    if(it.isNotEmpty())
                        isEmptyTotalAmount = false
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
                itemsIndexed(groupListModel.userList) { index, user ->
                    BillShareUser(user = user, billShareModel = billShareInputList[index] )
                }
            }

            Button(
                onClick = {
                    if(billName.isNullOrEmpty()){
                        isEmptyName = true
                    }else{
                        viewModel.addBill(
                            Bill(
                                groupListModel.group.id,
                                billName,
                                totalAmount.toFloat()
                            ),
                            billShareInputList
                        )
                        dialog?.cancel()
                    }
                },
                modifier = Modifier.constrainAs(btnAdd) {
                    top.linkTo(lcUsers.bottom, margin = 16.dp)
                    end.linkTo(parent.end)
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