package com.shashankbhat.splitbill.util.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.shashankbhat.splitbill.R

@Composable
fun BottomWarningText(
    modifier: Modifier,
    text: String,
    backgroundColor: Color = Color(0xFFA9B5FF)
) {

    Box(modifier = modifier.padding(8.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = 0.dp,
            shape = RoundedCornerShape(4.dp),
            backgroundColor = backgroundColor
        ) {
            Row(modifier = Modifier.padding(18.dp)) {
                Icon(
                    painter = painterResource(R.drawable.ic_outline_info),
                    contentDescription = text,
                    tint = Color.White
                )
                Text(
                    text = text,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

            }
        }
    }
}