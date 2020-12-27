package com.getmontir.customer.view.ui.splash

import android.content.Intent
import com.getmontir.customer.BuildConfig
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.getmontir.customer.R
import com.getmontir.customer.view.ui.AuthActivity
import com.getmontir.customer.view.ui.base.GetFragment
import com.getmontir.customer.viewmodel.SplashViewModel
import com.getmontir.lib.presentation.base.BaseFragment
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

open class SplashScreenFragment : GetFragment() {

    companion object {
        fun newInstance() = SplashScreenFragment()
    }

    private val viewModel: SplashViewModel by viewModel()

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
            val navController = findNavController()

            if( d ) {
                // Launch update activity
                navController.navigate(R.id.updateFragment)
            } else {
                if( !sessionManager.isLoggedIn ) {
                    // Launch Auth Activity
                    startActivity(
                        Intent(context, AuthActivity::class.java)
                    )
                    activity?.finish()
                } else {
                    // Launch Home Activity
                }
//                if( !sessionManager.isUsed ) {
//                    // Launch Walkthrough
//                    activity?.let {
//                        val intent = Intent(context, WalkthroughActivity::class.java)
//                        startActivity(intent)
//                        it.finish()
//                    }
//                } else {
//                    if( sessionManager.isLoggedIn ) {
//                        // Launch Home Activity
//                    } else {
//                        // Launch Auth Activity
//                        startActivity(
//                            Intent(context, AuthActivity::class.java)
//                        )
//                        activity?.finish()
//                    }
//                }
            }
        }
    }
}