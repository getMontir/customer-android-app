package com.getmontir.customer.view.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.getmontir.customer.R
import com.getmontir.customer.databinding.FragmentAuthForgotChangeBinding
import com.getmontir.customer.view.ui.base.GetFragment
import com.getmontir.customer.viewmodel.AuthViewModel
import com.getmontir.lib.ext.isPasswordConfirmation
import com.getmontir.lib.presentation.session
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ForgotChangePasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgotChangePasswordFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ForgotChangePasswordFragment.
         */
        @JvmStatic
        fun newInstance() = ForgotChangePasswordFragment()
    }

    private lateinit var binding: FragmentAuthForgotChangeBinding

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthForgotChangeBinding.inflate(inflater, container, false)
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
        viewModel.forgotToken.observe(viewLifecycleOwner, {
            processData("forgot", it)
        })

        // Setup listener
        binding.btnSend.setOnClickListener {
            doChange()
        }
    }

    override fun processResult(tag: String, data: Any?) {
        super.processResult(tag, data)

        if( tag == "forgot" ) {
            // Show logout
            showLoginFragment()
        }
    }

    @InternalCoroutinesApi
    private fun doChange() {
        if(
            binding.textInputPassword.isPasswordConfirmation(
                binding.textInputPasswordRepeat,
                "Harap isi kata sandi", "Minimal 6 karakter",
                "Kata sandi tidak sama"
            )
        ) {
            sessionManager.forgotToken?.let {
                viewModel.changePassword(
                    it,
                    binding.textInputPassword.text.toString().trim(),
                    binding.textInputPasswordRepeat.text.toString().trim()
                )
            }
        }
    }

    private fun showLoginFragment() {
        findNavController().popBackStack(R.id.loginFragment, false)
    }
}