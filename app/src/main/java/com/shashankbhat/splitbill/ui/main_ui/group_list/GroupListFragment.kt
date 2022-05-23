package com.shashankbhat.splitbill.ui.main_ui.group_list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.shashankbhat.splitbill.ui.theme.SplitBillTheme
import com.shashankbhat.splitbill.viewmodels.GroupListViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.activityViewModels
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.util.Status
import com.shashankbhat.splitbill.util.component.InstructionArrowText
import com.shashankbhat.splitbill.util.extension.getUniqueId
import com.shashankbhat.splitbill.util.extension.putToken
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupListFragment : Fragment() {

    private val viewModel: GroupListViewModel by activityViewModels()
    private lateinit var navController: NavController

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        navController = findNavController()
        viewModel.getAllGroups()

        viewModel.unauthorized.observe(viewLifecycleOwner) {
            if(it == true){
                viewModel.sharedPreferences.putToken("")
                navController.navigate(R.id.nav_splash_screen)
            }

        }

        return ComposeView(requireContext()).apply {
            setContent {
                SplitBillTheme {
                    GroupList()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.menu_unique_id).title = "# "+ viewModel.sharedPreferences.getUniqueId()
    }


    @OptIn(ExperimentalComposeUiApi::class)
    @ExperimentalMaterialApi
    @Composable
    fun GroupList() {

        val scaffoldState = rememberScaffoldState()
        val coroutineScope = rememberCoroutineScope()

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
            ) {

                val (lcGroup, ivNoData, ldProgress) = createRefs()

                if(viewModel.groupsListState.value.status == Status.Loading && viewModel.isTakingMoreTime.value){
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(ldProgress) {
                                top.linkTo(parent.top)
                            }
                    )
                }


                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(lcGroup) {
                            top.linkTo(ldProgress.bottom)
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

                if(viewModel.groupsListState.value.status == Status.Error){
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            viewModel.groupsListState.value.message ?: "",
                            ""
                        )
                    }
                }
            }

        }
    }

    companion object {
        @JvmStatic
        fun getInstance() = GroupListFragment()
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



