package com.shashankbhat.splitbill.ui.main_ui.nearby_people

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shashankbhat.splitbill.databinding.FragmentNearbyPeopleBinding
import com.shashankbhat.splitbill.util.LatLong
import com.shashankbhat.splitbill.util.LocationListener
import com.shashankbhat.splitbill.util.extension.findDistance
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NearbyPeopleFragment : Fragment() {
    private lateinit var binding: FragmentNearbyPeopleBinding
    private val myLocation = LocationListener()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNearbyPeopleBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.key.setOnClickListener {
            myLocation.getLocation(requireContext()){ location ->
                Log.i("NearbyPeopleFragment", "$location ${location.findDistance(LatLong(64.5802, 175.9521
                ))}")
                myLocation.stopLocationUpdate()
            }
        }

    }

    companion object {
        @JvmStatic
        fun getInstance() = NearbyPeopleFragment()
    }
}