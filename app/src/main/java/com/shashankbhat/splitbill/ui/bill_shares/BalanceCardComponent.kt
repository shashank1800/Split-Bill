package com.shashankbhat.splitbill.ui.bill_shares

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.ui.theme.Typography
import com.shashankbhat.splitbill.util.alogrithm.BillShareBalance

@Composable
fun BalanceCard(billShare: BillShareBalance){
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (tvPayedBy, tvPayment, tvPayedTo) = createRefs()

        Text(
            text = billShare.payedBy?.name ?: "",
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
                text = billShare.amountPayed.toString(),
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
            text = billShare.payedTo?.name ?: "",
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