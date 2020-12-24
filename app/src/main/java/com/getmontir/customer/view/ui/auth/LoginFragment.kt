package com.getmontir.customer.view.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.getmontir.customer.R
import com.getmontir.customer.databinding.FragmentAuthLoginBinding
import com.getmontir.customer.view.ui.base.GetFragment
import com.getmontir.customer.viewmodel.LoginViewModel
import com.getmontir.lib.ext.isNotNullOrEmpty
import com.getmontir.lib.ext.isPassword
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment LoginFragment.
         */
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    private lateinit var binding: FragmentAuthLoginBinding

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthLoginBinding.inflate( inflater, container, false )
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
        binding.btnSignIn.setOnClickListener {
            doLogin()
        }
        binding.divider.btnSocialGoogle.setOnClickListener {  }
        binding.divider.btnSocialFacebook.setOnClickListener {  }
        binding.textActionForgot.setOnClickListener {  }
    }

    override fun processResult(tag: String, data: Any?) {
        super.processResult(tag, data)

        if( tag == "token" ) {
            // Load user information
        }

        if( tag == "user" ) {
            // Launch main activity
            activity?.finish()
        }
    }

    @InternalCoroutinesApi
    private fun doLogin() {
        val email = binding.textInputEmail.text.toString().trim()
        val password = binding.textInputPassword.text.toString().trim()

        if(
            binding.textInputEmail.isNotNullOrEmpty("Harap isi email Anda")
            && binding.textInputPassword.isPassword("Harap isi kata sandi Anda")
        ) {
            binding.textLayoutEmail.error = null
            binding.textLayoutPassword.error = null
            viewModel.login(email, password)
        }
    }
}