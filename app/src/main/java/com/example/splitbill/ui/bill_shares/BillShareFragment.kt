package com.example.splitbill.ui.bill_shares

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.splitbill.ui.theme.SplitBillTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.splitbill.R
import com.example.splitbill.model.GroupListModel
import com.example.splitbill.util.extension.BillSplitAlgorithm
import com.example.splitbill.viewmodels.BillShareViewModel

@AndroidEntryPoint
class BillShareFragment : Fragment() {

    private val viewModel: BillShareViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var groupListModel: GroupListModel

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        navController = findNavController()

        groupListModel = requireArguments().getSerializable("model") as GroupListModel

        viewModel.getAllGroups(groupListModel.group.id)

        return ComposeView(requireContext()).apply {
            setContent {
                SplitBillTheme {
                    BillShares()
                }
            }
        }
    }


    @ExperimentalMaterialApi
    @Composable
    fun BillShares() {

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp, 8.dp, 8.dp, 4.dp),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val addBillDialog = AddBillSharesFragment(
                            groupListModel = groupListModel,
                            viewModel = viewModel,
                            navController = navController
                        )
                        addBillDialog.show(parentFragmentManager, addBillDialog.tag)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_receipt),
                        contentDescription = "Add Bill Shares",
                        tint = Color.White
                    )
                }
            },
        ) {

            Column(modifier = Modifier.fillMaxWidth()) {

                OutlinedButton(onClick = {
                    val algo = BillSplitAlgorithm(viewModel.billListState.value)
                    algo.splitBill()
                }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(text = "View Report")
                }

                LazyColumn {
                    itemsIndexed(viewModel.billListState.value) { index, bill ->
                        BillCard(billListDto = bill)
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
            BillShares()
        }
    }

}



