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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.splitbill.ui.theme.SplitBillTheme
import dagger.hilt.android.AndroidEntryPoint
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
                        val addBillDialog = AddBillSharesBottomSheetFragment(
                            groupListModel = groupListModel,
                            viewModel = viewModel,
                            navController = navController
                        )
                        addBillDialog.show(parentFragmentManager, addBillDialog.tag)
                    },
                    backgroundColor = Color(0xFF3EC590)
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

                if (viewModel.billListState.value.isNotEmpty())
                    OutlinedButton(onClick = {
                        val allCalculation = BillSplitAlgorithm(viewModel.billListState.value)
                        val billShareDialog = ShowBillSharesBottomSheetFragment(allCalculation)
                        billShareDialog.show(parentFragmentManager, billShareDialog.tag)
                    }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(text = "Balances")
                    }

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
                        itemsIndexed(viewModel.billListState.value) { index, bill ->
                            BillCard(billListDto = bill)
                        }
                    }
                    if (viewModel.billListState.value.isEmpty())
                        Box(modifier = Modifier
                            .padding(8.dp)
                            .constrainAs(ivNoData) {
                                bottom.linkTo(parent.bottom, margin = 60.dp)
                                end.linkTo(parent.end, margin = 90.dp)
                            }) {
                            Column {


                                Text(
                                    text = "TAP HERE TO  \n ADD BILLS",
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



