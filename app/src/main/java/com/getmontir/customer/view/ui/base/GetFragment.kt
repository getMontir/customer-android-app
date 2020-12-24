package com.getmontir.customer.view.ui.base

import androidx.navigation.fragment.findNavController
import com.getmontir.customer.R
import com.getmontir.lib.presentation.base.BaseFragment

open class GetFragment: BaseFragment() {

    override fun handleNetworkNoConnectivity(tag: String, e: Exception) {
        super.handleNetworkNoConnectivity(tag, e)
        findNavController().navigate(R.id.offlineFragment)
    }

    open fun navigateToUpdate() {
        findNavController().navigate(R.id.updateFragment)
    }
}