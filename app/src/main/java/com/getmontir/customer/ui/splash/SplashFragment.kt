package com.getmontir.customer.ui.splash

import android.content.Intent
import com.getmontir.customer.BuildConfig
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.getmontir.customer.R
import com.getmontir.customer.ui.walkthrough.WalkthroughActivity
import com.getmontir.customer.viewmodel.SplashViewModel
import com.getmontir.lib.presentation.fragment.BaseFragment
import com.getmontir.lib.presentation.utils.SessionManager
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

open class SplashFragment : BaseFragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private val viewModel: SplashViewModel by viewModel()

    private val sessionManager: SessionManager by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
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
        if( tag == "version") {
            Timber.tag("ASU").d(d.toString())

            if( d ) {
                // Launch update activity
            } else {
            }

            if( !sessionManager.isUsed ) {
                // Launch Walkthrough
                activity?.let {
                    val intent = Intent(context, WalkthroughActivity::class.java)
                    startActivity(intent)
                    it.finish()
                }
            } else {
                if( sessionManager.isLoggedIn ) {
                    // Launch Home Activity
                } else {
                    // Launch Auth Activity
                }
            }
        }
    }
}