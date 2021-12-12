package com.example.splitbill.ui.group_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.splitbill.model.Group
import com.example.splitbill.ui.theme.Typography

@Composable
fun GroupCard(group: Group = Group("Test", System.currentTimeMillis())) {
    Box(Modifier.padding(8.dp).fillMaxWidth()) {
        Card(
            elevation = 8.dp,
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(16.dp).fillMaxWidth()
            ) {
                val (tvGroupName) = createRefs()

                Text(
                    text = group.name,
                    modifier = Modifier.constrainAs(tvGroupName) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                    style = Typography.h6
                )

            }
        }
    }
}