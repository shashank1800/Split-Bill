package com.shashankbhat.splitbill.ui.bill_shares.balance

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.shashankbhat.splitbill.ui.theme.Typography
import com.shashankbhat.splitbill.util.alogrithm.BillSpentAndShare

@Composable
fun SpentAndShareCard(shareAndBalance: BillSpentAndShare) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (tvUserName, tvSpentAmount, tvShareAmount, tvBalance) = createRefs()

        Text(
            text = shareAndBalance.person?.name ?: "",
            modifier = Modifier.constrainAs(tvUserName) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(tvSpentAmount.start, margin = 16.dp)
                width = Dimension.fillToConstraints
            },
            style = Typography.body1
        )

        Text(
            text = shareAndBalance.spentAmount.toString(),
            modifier = Modifier.constrainAs(tvSpentAmount) {
                top.linkTo(parent.top)
                start.linkTo(tvUserName.end)
                end.linkTo(tvShareAmount.start, margin = 16.dp)
                width = Dimension.fillToConstraints
            },
            style = Typography.body1
        )

        Text(
            text = shareAndBalance.shareAmount.toString(),
            modifier = Modifier.constrainAs(tvShareAmount) {
                top.linkTo(parent.top)
                start.linkTo(tvSpentAmount.end)
                end.linkTo(tvBalance.end)
                width = Dimension.fillToConstraints
            },
            style = Typography.body1
        )

        Text(
            text = shareAndBalance.balanceAmount.toString(),
            modifier = Modifier.constrainAs(tvBalance) {
                top.linkTo(parent.top)
                start.linkTo(tvShareAmount.end)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                letterSpacing = 0.5.sp,
                color = if(shareAndBalance.balanceAmount >= 0F) Color(30, 141, 0, 255) else Color(192, 0, 65, 255)
            ),
        )
    }
}