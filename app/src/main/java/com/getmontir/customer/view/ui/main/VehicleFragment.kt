package com.getmontir.customer.view.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.getmontir.customer.R
import com.getmontir.customer.databinding.FragmentMainVehicleBinding
import com.getmontir.customer.view.ui.base.GetFragment


/**
 * A simple [Fragment] subclass.
 * Use the [VehicleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VehicleFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment VehicleFragment.
         */
        @JvmStatic
        fun newInstance() = VehicleFragment()
    }

    private lateinit var binding: FragmentMainVehicleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainVehicleBinding.inflate( inflater, container, false )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        binding.toolbar.setupWithNavController( findNavController() )
    }
}