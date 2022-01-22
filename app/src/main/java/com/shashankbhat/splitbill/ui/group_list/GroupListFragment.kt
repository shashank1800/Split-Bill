package com.shashankbhat.splitbill.ui.group_list

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
import com.shashankbhat.splitbill.ui.theme.SplitBillTheme
import com.shashankbhat.splitbill.viewmodels.GroupListViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.shashankbhat.splitbill.util.Status
import com.shashankbhat.splitbill.util.component.InstructionArrowText

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
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {

                val (lcGroup, ivNoData, ldProgress) = createRefs()

                if(viewModel.groupsListState.value.status == Status.Loading)
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(ldProgress) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(lcGroup) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    items(viewModel.groupsListState.value.data ?: emptyList()) { group ->
                        GroupCard(group = group, scaffoldState, navController)
                    }
                }

                if (viewModel.groupsListState.value.data?.isEmpty() == true)
                    InstructionArrowText(
                        modifier = Modifier
                            .padding(8.dp)
                            .constrainAs(ivNoData) {
                                bottom.linkTo(parent.bottom, margin = 60.dp)
                                end.linkTo(parent.end, margin = 90.dp)
                            },
                        text = "TAP HERE TO  \n  ADD GROUP"
                    )
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



