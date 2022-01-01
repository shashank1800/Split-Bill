package com.example.splitbill.ui.group_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.splitbill.ui.theme.SplitBillTheme
import com.example.splitbill.viewmodels.GroupListViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.splitbill.R
import com.example.splitbill.ui.theme.Typography

@AndroidEntryPoint
class GroupListFragment : Fragment() {

    private val viewModel: GroupListViewModel by viewModels()
    private lateinit var navController: NavController

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        navController = findNavController()
        viewModel.getAllGroups()

        return ComposeView(requireContext()).apply {
            setContent {
                SplitBillTheme {
                    GroupList()
                }
            }
        }
    }


    @ExperimentalMaterialApi
    @Composable
    fun GroupList() {

        val scaffoldState = rememberScaffoldState()

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            scaffoldState = scaffoldState,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val addGroupDialog = AddGroupFragment(viewModel)
                        addGroupDialog.show(parentFragmentManager, addGroupDialog.tag)
                    },
                    backgroundColor = Color(0xFF3EC590)
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "Add Group",
                        tint = Color.White
                    )
                }
            },
            topBar = {
                TopAppBar {
                    Column(Modifier.padding(horizontal = 8.dp)) {
                        Text(
                            text = "Split Bill", style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                            )
                        )
                    }
                }
            }
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {

                val (lcGroup, ivNoData) = createRefs()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(lcGroup) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    items(viewModel.groupsListState.value) { group ->
                        GroupCard(group = group, scaffoldState, navController)
                    }
                }

                if (viewModel.groupsListState.value.isEmpty())
                    Box(modifier = Modifier
                        .padding(8.dp)
                        .constrainAs(ivNoData) {
                            bottom.linkTo(parent.bottom, margin = 60.dp)
                            end.linkTo(parent.end, margin = 90.dp)
                        }) {
                        Column {


                            Text(
                                text = "TAP HERE TO  \n  ADD GROUP",
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
                                contentDescription = "add member",
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(100.dp)
                                    .align(Alignment.End)
                            )
                        }
                    }
            }

        }
    }


    @ExperimentalMaterialApi
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        SplitBillTheme {
            GroupList()
        }
    }

}



