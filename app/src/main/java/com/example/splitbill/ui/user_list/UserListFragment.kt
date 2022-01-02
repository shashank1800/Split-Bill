package com.example.splitbill.ui.user_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.splitbill.ui.theme.SplitBillTheme
import com.example.splitbill.viewmodels.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.splitbill.R
import com.example.splitbill.model.GroupListModel
import com.example.splitbill.ui.theme.Typography
import com.example.splitbill.util.component.BottomWarningText
import com.example.splitbill.util.extension.findActivity

@AndroidEntryPoint
class UserListFragment : Fragment() {

    private val viewModel: UserListViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var groupListModel: GroupListModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        navController = findNavController()

        groupListModel = requireArguments().getSerializable("model") as GroupListModel
        viewModel.getAllUsersByGroupId(groupListModel.group.id)
        viewModel.getAllGroups(groupListModel.group.id)

        return ComposeView(requireContext()).apply {
            setContent {
                SplitBillTheme {
                    UserList()
                }
            }
        }
    }


    @Composable
    fun UserList() {

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            floatingActionButton = {
                if (viewModel.billListState.value.isEmpty())
                    FloatingActionButton(
                        onClick = {
                            val addMember = AddGroupMemberFragment(viewModel, groupListModel)
                            context?.findActivity()?.supportFragmentManager?.let {
                                addMember.show(
                                    it,
                                    addMember.tag
                                )
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_person_add),
                            contentDescription = "Add Member",
                            tint = Color.White
                        )
                    }
            },
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {

                val (lcGroup, ivNoData, tvNoData) = createRefs()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(lcGroup) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    items(viewModel.userListState.value) { user ->
                        UserCard(user = user, viewModel)
                    }
                }


                BottomWarningText(
                    modifier = Modifier
                        .constrainAs(ivNoData) {
                            bottom.linkTo(parent.bottom, margin = 70.dp)
                            end.linkTo(parent.end)
                        },
                    text = if (viewModel.billListState.value.isEmpty())
                        "Note : You cannot add people after adding bills and shares to the group, So please make sure that you are adding all the people before adding any bill."
                    else "You cannot add users after adding bills and shares to group",
                    backgroundColor = if (viewModel.billListState.value.isEmpty()) Color(0xFFA9B5FF) else Color(
                        0xFFFF8B9C
                    )
                )


                if (viewModel.userListState.value.isEmpty())
                    Box(modifier = Modifier
                        .constrainAs(tvNoData) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }) {
                        Text(
                            text = "No users found",
                            style = TextStyle(
                                fontFamily = FontFamily(
                                    Font(R.font.cabin_sketch, FontWeight.Normal),
                                ),
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
                                color = Color(0xFF818181)
                            )
                        )
                    }
            }

        }
    }

}