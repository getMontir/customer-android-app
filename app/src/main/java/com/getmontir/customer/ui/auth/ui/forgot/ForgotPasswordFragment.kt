package com.getmontir.customer.ui.auth.ui.forgot

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.getmontir.customer.R
import com.getmontir.customer.viewmodel.ForgotPasswordViewModel
import com.getmontir.lib.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

open class ForgotPasswordFragment : BaseFragment() {

    companion object {
        fun newInstance() = ForgotPasswordFragment()
    }

    private val viewModel: ForgotPasswordViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.forgot_password_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}