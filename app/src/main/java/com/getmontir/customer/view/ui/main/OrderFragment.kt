package com.getmontir.customer.view.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.getmontir.customer.R
import com.getmontir.customer.databinding.FragmentMainOrderBinding
import com.getmontir.customer.view.adapter.main.TabOrderAdapter
import com.getmontir.customer.view.ui.base.GetFragment
import com.google.android.material.tabs.TabLayoutMediator

/**
 * A simple [Fragment] subclass.
 * Use the [OrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment OrderFragment.
         */
        @JvmStatic
        fun newInstance() = OrderFragment()
    }

    private lateinit var binding: FragmentMainOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Toolbar
        val toolbar: Toolbar = binding.toolbar
        val navController = findNavController()
        binding.collapsingToolbar.setupWithNavController(toolbar, navController, AppBarConfiguration(navController.graph))
        binding.collapsingToolbar.title = resources.getString(R.string.navigation_order)

        // Setup view
        binding.viewPager.adapter = TabOrderAdapter(this)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            when( position ) {
                0 -> {
                    tab.text = "On Progress"
                }
                else -> {
                    tab.text = "History"
                }
            }
        }.attach()
    }
}