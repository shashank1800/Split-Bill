package com.example.splitbill.ui.user_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.splitbill.R
import com.example.splitbill.room_db.entity.User
import com.example.splitbill.ui.theme.Typography
import com.example.splitbill.viewmodels.UserListViewModel

@Composable
fun UserCard(
    user: User,
    viewModel: UserListViewModel
) {

    Box(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Card(
            elevation = 2.dp,
            shape = RoundedCornerShape(6.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                val (tvUserName, btnDeleteUser) = createRefs()

                Text(
                    text = user.name,
                    modifier = Modifier
                        .constrainAs(tvUserName) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(btnDeleteUser.start)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        },
                    style = Typography.h6,
                )

                IconButton(
                    onClick = {
                        viewModel.deleteUser(user)
                    },
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .constrainAs(btnDeleteUser) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_delete),
                        contentDescription = "delete member",
                        tint = Color(0xFF3EC590)
                    )
                }

            }
        }
    }
}