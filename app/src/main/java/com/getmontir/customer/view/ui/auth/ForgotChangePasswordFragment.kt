package com.getmontir.customer.view.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import timber.log.Timber

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
        binding.textInputPasswordRepeat.setOnEditorActionListener { _, actionId, _ ->
            if( actionId == EditorInfo.IME_ACTION_DONE ) {
                binding.btnSend.performClick()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
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
                getString(R.string.error_field_password_empty),
                getString(R.string.error_field_password_confirmation_empty),
                getString(R.string.error_field_password_length),
                getString(R.string.error_field_password_confirmation)
            )
        ) {
            sessionManager.forgotToken?.let {
                sessionManager.forgotEmail?.let { it1 ->
                    viewModel.changePassword(
                            it,
                            binding.textInputPassword.text.toString().trim(),
                            binding.textInputPasswordRepeat.text.toString().trim(),
                            it1
                    )
                }
            }
        }
    }

    private fun showLoginFragment() {
        findNavController().popBackStack(R.id.loginFragment, false)
    }

    /**
     * Invalid token
     */
    override fun handleHttpBadRequest(tag: String, e: Exception) {
        super.handleHttpBadRequest(tag, e)
        if( tag == "token" ) {
            Timber.tag("BISMILLAH").e("Invalid token")
        }
    }

    /**
     * User not found
     */
    override fun handleHttpNotFound(tag: String, e: Exception) {
        if( tag == "token" ) {
            Timber.tag("BISMILLAH").e("Invalid user")
        }
    }
}