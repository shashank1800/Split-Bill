package com.example.splitbill.ui.bill_shares

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.splitbill.R
import com.example.splitbill.ui.theme.SplitBillTheme
import com.example.splitbill.ui.theme.Typography
import com.example.splitbill.util.extension.BillSplitAlgorithm
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ShowBillSharesBottomSheetFragment(private val billSplitAlgorithm: BillSplitAlgorithm) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                SplitBillTheme {
                    ShowBillShares()
                }
            }
        }
    }


    @Composable
    fun ShowBillShares() {

        ConstraintLayout(
            modifier = Modifier
                .padding(8.dp)
        ) {
            val (tvCreateGroup, lcBillShares) = createRefs()

            Text(
                text = "Balance",
                modifier = Modifier.constrainAs(tvCreateGroup) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
                style = Typography.h6
            )

            LazyColumn(modifier = Modifier.constrainAs(lcBillShares){
                top.linkTo(tvCreateGroup.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                width = Dimension.fillToConstraints
            }) {
                items(billSplitAlgorithm.getBalances() ?: emptyList()) { billShare ->
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (tvPayedBy, tvPayment, tvPayedTo) = createRefs()

                        Text(
                            text = billShare.payedBy.billDetails?.user_name ?: "",
                            modifier = Modifier.constrainAs(tvPayedBy) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(tvPayment.start, margin = 16.dp)
                                width = Dimension.fillToConstraints
                            },
                            style = Typography.body1
                        )

                        Row(modifier = Modifier.constrainAs(tvPayment) {
                            top.linkTo(parent.top)
                            start.linkTo(tvPayedBy.end)
                            end.linkTo(tvPayedTo.start, margin = 16.dp)
                            width = Dimension.fillToConstraints
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_remove),
                                contentDescription = "minus",
                                tint = Color.Gray
                            )

                            Text(
                                text = "\u20B9" + billShare.amountPayed.toString(),
                                style = TextStyle(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    color = Color(0xFF3EC590)
                                )
                            )

                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_arrow_forward),
                                contentDescription = "arrow",
                                tint = Color.Gray
                            )
                        }

                        Text(
                            text = billShare.payedTo.billDetails?.user_name ?: "",
                            modifier = Modifier.constrainAs(tvPayedTo) {
                                top.linkTo(parent.top)
                                start.linkTo(tvPayment.end)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                            },
                            style = Typography.body1
                        )
                    }

                }
            }


        }

    }

//    @Preview(showBackground = true)
//    @Composable
//    fun ShowBillSharesPreview() {
//        SplitBillTheme {
//            ShowBillShares()
//        }
//    }


}
