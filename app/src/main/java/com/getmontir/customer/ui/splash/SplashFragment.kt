package com.getmontir.customer.ui.splash

import com.getmontir.customer.BuildConfig
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.getmontir.customer.R
import com.getmontir.customer.viewmodel.SplashViewModel
import com.getmontir.lib.presentation.fragment.BaseFragment
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SplashFragment : BaseFragment() {

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
            processData("version", it)
        })

        viewModel.loadCustomerVersion( BuildConfig.VERSION_CODE )
    }

    override fun processResult(tag: String, data: Any?) {
        super.processResult(tag, data)
        val d = data as Boolean
        if( tag == "version") Timber.tag("ASU").d(d.toString())
    }
}