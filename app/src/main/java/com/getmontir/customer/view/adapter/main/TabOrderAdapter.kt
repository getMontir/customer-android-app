package com.getmontir.customer.view.adapter.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.getmontir.customer.view.ui.main.order.OrderHistoryFragment
import com.getmontir.customer.view.ui.main.order.OrderOnProgressFragment

class TabOrderAdapter(fm: Fragment): FragmentStateAdapter(fm) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment: Fragment = when( position ) {
            1 -> {
                OrderHistoryFragment()
            }
            else -> {
                OrderOnProgressFragment()
            }
        }

//        val fragment = DemoObjectFragment()
//        fragment.arguments = Bundle().apply {
//            // Our object is just an integer :-P
//            putInt(ARG_OBJECT, position + 1)
//        }
        return fragment
    }
}