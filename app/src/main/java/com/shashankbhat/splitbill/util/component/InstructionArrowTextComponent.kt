package com.shashankbhat.splitbill.util.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shashankbhat.splitbill.R

@Composable
fun InstructionArrowText(
    modifier: Modifier,
    text: String
) {
    Box(modifier = modifier) {
        Column {

            Text(
                text = text,
                style = TextStyle(
                    fontFamily = FontFamily(
                        Font(R.font.cabin_sketch, FontWeight.Normal),
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color(0xFF818181)
                )
            )

            Icon(
                painter = painterResource(R.drawable.ic_right_drawn_arrow),
                tint = Color(0xFF818181),
                contentDescription = text,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .align(Alignment.End)
            )
        }
    }
}