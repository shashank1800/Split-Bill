package com.shashankbhat.splitbill.ui.main_ui.profile

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.shashankbhat.splitbill.databinding.FragmentProfileBinding
import com.shashankbhat.splitbill.enums.SnackBarType
import com.shashankbhat.splitbill.database.local.model.profile.DistanceRangeModel
import com.shashankbhat.splitbill.util.bottom_sheet.BottomSheetItem
import com.shashankbhat.splitbill.util.bottom_sheet.SingleItemSelectionBottomSheet
import com.shashankbhat.splitbill.util.extension.getBottomSheetList
import com.shashankbhat.splitbill.util.extension.getColorV2
import com.shashankbhat.splitbill.util.extension.showSnackBar
import com.shashankbhat.splitbill.viewmodels.MainScreenViewModel


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: MainScreenViewModel by activityViewModels()

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

        /** Ui when profile data is not available **/
        uiProfileImageClickListener()
        uiNameTextDoOnTextChanged()
        uiNearbyRangeButtonClickListener()
        uiSaveButtonClickListener()

        /** Ui when profile data is available **/
        uiProfileImageClickListenerWithData()
        networkUpdatePhotoResponse()
    }

    private fun uiNearbyRangeButtonClickListener(){
        binding.layoutEmptyData.btnNearbyRange.setOnClickListener {
            val dialog = SingleItemSelectionBottomSheet(viewModel.distanceList.getBottomSheetList(),
                object : SingleItemSelectionBottomSheet.EventListeners<DistanceRangeModel> {
                    override fun onClick(position: Int, item: BottomSheetItem<DistanceRangeModel>) {
                        viewModel.distanceRange.set(item.originalItem)
                    }
                }, viewModel.distanceRange)
            dialog.show(parentFragmentManager, dialog.tag)
        }
    }

    private fun uiSaveButtonClickListener(){
        binding.layoutEmptyData.btnSave.setOnClickListener {
            if(isFieldsValid()){
                viewModel.saveProfile()
            }
        }
    }

    private fun uiProfileImageClickListener(){
        binding.layoutEmptyData.ivProfilePhoto.setOnClickListener {
            val dialog = ProfileSelectBottomSheetFragment(viewModel.iconList){
                viewModel.profilePhoto.set(it)
            }
            dialog.show(parentFragmentManager, dialog.tag)
        }
    }

    private fun uiNameTextDoOnTextChanged(){
        binding.layoutEmptyData.tvName.doOnTextChanged { text, _, _, _ ->
            if(!text.isNullOrEmpty())
                binding.layoutEmptyData.cvProfilePhoto.setCardBackgroundColor(text.toString().getColorV2())
            else
                binding.layoutEmptyData.cvProfilePhoto.setCardBackgroundColor(Color.WHITE)
        }
    }


    private fun uiProfileImageClickListenerWithData(){
        binding.layoutWithData.ivProfilePhoto.setOnClickListener {
            val dialog = ProfileSelectBottomSheetFragment(viewModel.iconList){
                viewModel.updateProfilePhoto(it)
            }
            dialog.show(parentFragmentManager, dialog.tag)
        }
    }


    private fun networkUpdatePhotoResponse(){
        viewModel.updateProfilePhotoResponse.observe(viewLifecycleOwner){
            when{
                it.isError() -> {
                    binding.showSnackBar("Please check your internet", snackBarType = SnackBarType.INSTRUCTION)
                }
            }
        }
    }

    private fun isFieldsValid(): Boolean{

        if(viewModel.profilePhoto.get() == null || viewModel.profilePhoto.get()?.url.isNullOrEmpty()){
            binding.showSnackBar("Please select profile icon", snackBarType = SnackBarType.ERROR)
            return false
        }

        if(viewModel.fullName.get() == null || viewModel.fullName.get().isNullOrEmpty()){
            binding.showSnackBar("Please enter full name", snackBarType = SnackBarType.ERROR)
            return false
        }

        if(viewModel.isNearbyEnabled.get() && viewModel.distanceRange.get() == null){
            binding.showSnackBar("Please select distance range", snackBarType = SnackBarType.ERROR)
            return false
        }

        return true
    }

    companion object {
        @JvmStatic
        fun getInstance() = ProfileFragment()
    }
}