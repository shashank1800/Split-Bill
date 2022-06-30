package com.shashankbhat.splitbill.ui.main_ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.shashankbhat.splitbill.databinding.FragmentProfileBinding
import com.shashankbhat.splitbill.model.profile.DistanceRangeModel
import com.shashankbhat.splitbill.util.bottom_sheet.BottomSheetItem
import com.shashankbhat.splitbill.util.bottom_sheet.SingleItemSelectionBottomSheet
import com.shashankbhat.splitbill.util.extension.getBottomSheetList
import com.shashankbhat.splitbill.viewmodels.GroupListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: GroupListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        binding.btnNearbyRange.setOnClickListener {
            val dialog = SingleItemSelectionBottomSheet(viewModel.distanceList.getBottomSheetList(),
                object : SingleItemSelectionBottomSheet.EventListeners<DistanceRangeModel> {
                    override fun onClick(position: Int, item: BottomSheetItem<DistanceRangeModel>) {
                        viewModel.distanceRange.set(item.originalItem)
                    }
                }, viewModel.distanceRange)
            dialog.show(parentFragmentManager, dialog.tag)
        }

        binding.btnEdit.setOnClickListener {
            viewModel.isEditEnabled.set(true)
        }

        binding.btnCancel.setOnClickListener {
            viewModel.isEditEnabled.set(false)
        }

        binding.btnSave.setOnClickListener {
            viewModel.isEditEnabled.set(false)

            viewModel.saveProfile(binding.tvName.text.toString())
        }

        binding.ivProfilePhoto.setOnClickListener {
            val dialog = ProfileSelectBottomSheetFragment(viewModel.iconList){
                viewModel.profilePhoto.set(it)
            }
            dialog.show(parentFragmentManager, dialog.tag)
        }
    }

    companion object {
        @JvmStatic
        fun getInstance() = ProfileFragment()
    }
}