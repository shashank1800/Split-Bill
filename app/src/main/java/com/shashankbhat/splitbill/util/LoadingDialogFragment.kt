package com.shashankbhat.splitbill.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.shashankbhat.splitbill.databinding.FragmentLoadingDialogBinding

class LoadingDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentLoadingDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Light)
        binding = FragmentLoadingDialogBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoadingDialogFragment()
    }
}