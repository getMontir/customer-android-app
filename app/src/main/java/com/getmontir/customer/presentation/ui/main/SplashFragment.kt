package com.getmontir.customer.presentation.ui.main

import com.getmontir.customer.BuildConfig
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.getmontir.customer.R
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    protected val viewModel: SplashViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    @InternalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.customerVersion.observe( viewLifecycleOwner, {
            Timber.tag("ASU").d(it.toString())
        })

        viewModel.loadCustomerVersion( BuildConfig.VERSION_CODE )
    }

}