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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.splitbill.model.Group
import com.example.splitbill.ui.theme.Typography


@AndroidEntryPoint
class GroupListFragment : Fragment() {

    private val viewModel: GroupListViewModel by viewModels()
    private lateinit var navController: NavController

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


    @Composable
    fun GroupList() {

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val addGroupDialog = AddGroupFragment(viewModel)
                        addGroupDialog.show(parentFragmentManager, addGroupDialog.tag)
                    }
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "Localized description",
                        tint = Color.White
                    )
                }
            },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(viewModel.groupsListState.value) { group ->
                    GroupCard(group = group)
                }
            }

        }
    }

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

    @Preview(showBackground = true)
    @Composable
    fun GroupCardPreview() {
        GroupCard()
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        SplitBillTheme {
            GroupList()
        }
    }

}



