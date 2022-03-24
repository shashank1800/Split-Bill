package com.shashankbhat.splitbill.ui.bill_shares

import android.os.Bundle
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
import com.shashankbhat.splitbill.ui.theme.SplitBillTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.base.TitleFragment
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.util.component.InstructionArrowText
import com.shashankbhat.splitbill.util.alogrithm.BillSplitAlgorithm
import com.shashankbhat.splitbill.viewmodels.BillShareViewModel

@AndroidEntryPoint
class BillShareFragment : TitleFragment() {

    private val viewModel: BillShareViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var groupListDto: GroupListDto

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        navController = findNavController()
        groupListDto = requireArguments().getSerializable("model") as GroupListDto

        setTitle(groupListDto.group.name)

        viewModel.getAllBill(groupListDto.group.id ?: -1)

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
                            groupListDto = groupListDto,
                            viewModel = viewModel
                        )
                        addBillDialog.show(parentFragmentManager, addBillDialog.tag)
                    },
                    backgroundColor = Color(0xFF3EC590)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_add_bill),
                        contentDescription = "Add Bill Shares",
                        tint = Color.White
                    )
                }
            },
        ) {

            Column(modifier = Modifier.fillMaxWidth()) {

                if (viewModel.billList.value.data?.isNotEmpty()==true)
                    OutlinedButton(onClick = {
                        viewModel.billList.value.data?.let {
                            val allCalculation = BillSplitAlgorithm(it)
                            val billShareDialog = ShowBillSharesBottomSheetFragment(allCalculation)
                            billShareDialog.show(parentFragmentManager, billShareDialog.tag)
                        }

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
                        itemsIndexed(viewModel.billList.value.data ?: emptyList()) { index, bill ->
                            BillCard(billModel = bill, viewModel = viewModel)
                        }
                    }
                    if (viewModel.billList.value.data?.isEmpty() == true)
                        InstructionArrowText(
                            modifier = Modifier
                                .padding(8.dp)
                                .constrainAs(ivNoData) {
                                    bottom.linkTo(parent.bottom, margin = 60.dp)
                                    end.linkTo(parent.end, margin = 90.dp)
                                },
                            text = "TAP HERE TO  \n ADD BILLS"
                        )
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



