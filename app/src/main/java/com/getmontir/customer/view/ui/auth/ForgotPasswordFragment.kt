package com.getmontir.customer.view.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.getmontir.customer.R
import com.getmontir.customer.databinding.FragmentAuthForgotPasswordBinding
import com.getmontir.customer.view.ui.base.GetFragment
import com.getmontir.customer.viewmodel.AuthViewModel
import com.getmontir.lib.ext.isEmailNotNull
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ForgotPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgotPasswordFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ForgotPasswordFragment.
         */
        @JvmStatic
        fun newInstance() = ForgotPasswordFragment()
    }

    private lateinit var binding: FragmentAuthForgotPasswordBinding

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAuthForgotPasswordBinding.inflate( inflater, container, false )
        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Toolbar
        val toolbar: Toolbar = binding.toolbar
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment)

        // Setup view model
        viewModel.token.observe(viewLifecycleOwner, {
            processData("token", it)
        })

        // Setup listener
        binding.btnSend.setOnClickListener {
            doReset()
        }
    }

    override fun processResult(tag: String, data: Any?) {
        super.processResult(tag, data)
        if( tag == "token" ) {
            val token = data as String
            sessionManager.token = token

            // Show fragment verification
        }
    }

    @InternalCoroutinesApi
    private fun doReset() {
        if( binding.textInputEmail.isEmailNotNull("Harap isi email Anda", "Harap masukan alamat email yang valid") ) {
            viewModel.forgotPassword(binding.textInputEmail.text.toString().trim())
        }
    }
}