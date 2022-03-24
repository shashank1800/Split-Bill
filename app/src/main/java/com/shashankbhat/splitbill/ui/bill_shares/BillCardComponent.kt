package com.shashankbhat.splitbill.ui.bill_shares

import android.text.format.DateUtils.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.shashankbhat.splitbill.database.local.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.ui.theme.SplitBillTheme
import com.shashankbhat.splitbill.ui.theme.Typography
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.extension.badgeLayout
import com.shashankbhat.splitbill.util.extension.getColor
import com.shashankbhat.splitbill.util.extension.getUniqueId
import com.shashankbhat.splitbill.viewmodels.BillShareViewModel

@ExperimentalMaterialApi
@Composable
fun BillCard(
    billModel: BillModel = BillModel(),
    viewModel: BillShareViewModel? = null
) {

    val context = LocalContext.current

    val openRemoveDialog = remember {
        mutableStateOf(false)
    }

    DeleteBillAlertDialog(billModel, viewModel, openRemoveDialog)

    Box(
        Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp),
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                val (tvBillName, tvTotalAmount, lcShare, tvTime, btnRemove) = createRefs()

                Text(
                    text = billModel.name ?: "",
                    modifier = Modifier
                        .constrainAs(tvBillName) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(tvTotalAmount.start)
                            width = Dimension.fillToConstraints
                        },
                    style = Typography.h6
                )

                Text(
                    text = "Total " + billModel.totalAmount.toString(),
                    modifier = Modifier
                        .constrainAs(tvTotalAmount) {
                            top.linkTo(parent.top)
                            linkTo(tvBillName.end, parent.end, bias = 0F)
                            width = Dimension.wrapContent
                        },
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF2196F3)
                    )
                )

                LazyColumn(
                    modifier = Modifier
                        .heightIn(min = 50.dp, max = 700.dp)
                        .constrainAs(lcShare) {
                            top.linkTo(tvBillName.bottom)
                            start.linkTo(parent.start)
                        },
                ) {
                    items(billModel.billShares ?: emptyList()) { billShare ->
                        ConstraintLayout(
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 8.dp)
                                .fillMaxWidth()
                        ) {
                            val (bIcon, tvName, tvSpent, clShareSpent) = createRefs()

                            Text(
                                text = if (billShare.user?.name?.length ?: 0 > 0) billShare.user?.name?.get(0).toString().uppercase() else "",
                                modifier = Modifier
                                    .background(
                                        (billShare.user?.name ?: "").getColor(),
                                        shape = CircleShape
                                    )
                                    .badgeLayout()
                                    .constrainAs(bIcon) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                    },
                                style = Typography.subtitle1,
                                color = Color.White
                            )

                            Text(
                                text = billShare.user?.name ?: "",
                                style = Typography.body1,
                                modifier = Modifier
                                    .padding(10.dp, 0.dp)
                                    .constrainAs(tvName) {
                                        top.linkTo(parent.top)
                                        start.linkTo(bIcon.end)
                                        width = Dimension.wrapContent
                                    }

                            )

                            if (billShare.spent ?: 0F > 0)
                                Text(
                                    text = "Spent " + billShare.spent.toString(),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        letterSpacing = 0.4.sp,
                                        color = Color(0, 129, 69, 255)
                                    ),
                                    modifier = Modifier
                                        .padding(10.dp, 0.dp)
                                        .constrainAs(tvSpent) {
                                            top.linkTo(tvName.bottom)
                                            start.linkTo(tvName.start)
                                            width = Dimension.wrapContent
                                        }
                                )

                            Column(modifier = Modifier
                                .constrainAs(clShareSpent) {
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                    width = Dimension.wrapContent
                                }) {

                                Text(
                                    text = "Share " + billShare.share.toString(),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        letterSpacing = 0.4.sp,
                                    ),
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }


                        }
                    }
                }

                if (billModel.uniqueId == viewModel?.sharedPreferences?.getUniqueId())
                    OutlinedButton(modifier = Modifier
                        .constrainAs(btnRemove) {
                            top.linkTo(lcShare.bottom)
                            start.linkTo(parent.start)
                            width = Dimension.wrapContent
                        },
                        onClick = {
                            openRemoveDialog.value = true
                        }) {
                        Text(text = "Remove")
                    }

                Text(
                    text = getRelativeDateTimeString(
                        context,
                        billModel.dateCreated ?: System.currentTimeMillis(),
                        MINUTE_IN_MILLIS,
                        WEEK_IN_MILLIS,
                        FORMAT_SHOW_TIME
                    ).toString(),
                    style = Typography.caption,
                    modifier = Modifier
                        .constrainAs(tvTime) {
                            bottom.linkTo(btnRemove.bottom)
                            end.linkTo(parent.end)
                            width = Dimension.wrapContent
                        }
                )

            }
        }
    }

}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SplitBillTheme {
        BillCard()
    }
}

@Composable
fun DeleteBillAlertDialog(
    billModel: BillModel = BillModel(),
    viewModel: BillShareViewModel? = null,
    openRemoveDialog: MutableState<Boolean>
) {
    if (openRemoveDialog.value) {
        AlertDialog(
            onDismissRequest = { openRemoveDialog.value = false },
            title = { Text("Remove") },
            text = { Text("Are you sure, want to delete \"${billModel.name}\" ?") },
            confirmButton = {
                Button(
                    onClick = {
                        openRemoveDialog.value = false

                        viewModel?.deleteBill(billModel)
                        val deletedList = viewModel?.billList?.value?.data?.filter { it != billModel }?.toMutableList()
                        viewModel?.billList?.value = Response.success(deletedList)
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        openRemoveDialog.value = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}