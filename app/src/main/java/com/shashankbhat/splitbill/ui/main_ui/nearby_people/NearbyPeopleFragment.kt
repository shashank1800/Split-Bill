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
                /*
                * 0.4 0.4 - 48 km
                * 0 0.1 - 4.87
                * 0.1 0.1 - 12.1
                * */
                val locationFrom = LatLong(64.0, 175.0)
                val locationTo = LatLong(64.0, 177.5)
                Log.i("NearbyPeopleFragment", " ${locationFrom.findDistance(locationTo)}")
                myLocation.stopLocationUpdate()
            }
        }

    }

    companion object {
        @JvmStatic
        fun getInstance() = NearbyPeopleFragment()
    }
}