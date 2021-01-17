package com.getmontir.customer.view.ui.main.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.getmontir.customer.R
import com.getmontir.customer.databinding.FragmentMainOrderHistoryBinding
import com.getmontir.customer.view.ui.base.GetFragment

/**
 * A simple [Fragment] subclass.
 * Use the [OrderHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderHistoryFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment OrderHistoryFragment.
         */
        @JvmStatic
        fun newInstance() = OrderHistoryFragment()
    }

    private lateinit var binding: FragmentMainOrderHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainOrderHistoryBinding.inflate( inflater, container, false )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup listener
        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
        }
    }

}