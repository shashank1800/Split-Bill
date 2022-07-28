package com.shashankbhat.splitbill.ui.bill_shares.shares

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.*
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shashankbhat.splitbill.BR
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.base.TitleFragment
import com.shashankbhat.splitbill.database.local.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.databinding.AdapterBillShareBinding
import com.shashankbhat.splitbill.databinding.FragmentBillShareBinding
import com.shashankbhat.splitbill.ui.bill_shares.add_bill.AddBillSharesBottomSheetFragment
import com.shashankbhat.splitbill.viewmodels.BillShareViewModel

@AndroidEntryPoint
class BillShareFragment : TitleFragment() {

    private val viewModel: BillShareViewModel by activityViewModels()
    private lateinit var groupListDto: GroupListDto
    private lateinit var binding: FragmentBillShareBinding
    lateinit var adapter: RecyclerGenericAdapter<AdapterBillShareBinding, BillModel>

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillShareBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupListDto = requireArguments().getSerializable("model") as GroupListDto

        setTitle(groupListDto.group.name)
        binding.isBillListEmpty = viewModel.isBillListEmpty

        viewModel.getAllBill(groupListDto.group.id ?: -1)

        binding.fab.setOnClickListener {
            showAddBillBottomSheet()
        }

        adapter = RecyclerGenericAdapter.Builder<AdapterBillShareBinding, BillModel>(R.layout.adapter_bill_share, BR.model)
            .build()

        binding.rvNearbyUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNearbyUsers.adapter = adapter

        viewModel.billList.observe(viewLifecycleOwner) {
            if(it.isSuccess()) {
                adapter.replaceList(ArrayList(it.data ?: emptyList()))
                viewModel.isBillListEmpty.set(it.data?.size == 0)
            }
        }
    }

    private fun showAddBillBottomSheet(){
        val addBillDialog = AddBillSharesBottomSheetFragment(
            groupListDto = groupListDto,
            viewModel = viewModel
        )
        addBillDialog.show(parentFragmentManager, addBillDialog.tag)
    }


    companion object {
        fun getInstance(bundle: Bundle): BillShareFragment {
            val fragment = BillShareFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}



