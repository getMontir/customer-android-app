package com.getmontir.customer.view.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.getmontir.customer.R
import com.getmontir.customer.databinding.FragmentAuthForgotConfirmBinding
import com.getmontir.customer.view.ui.base.GetFragment
import com.getmontir.customer.viewmodel.AuthViewModel
import com.getmontir.lib.ext.isNotNullAndMinLength
import com.getmontir.lib.ext.isNotNullOrEmpty
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [ForgotConfirmFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgotConfirmFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ForgotConfirmFragment.
         */
        @JvmStatic
        fun newInstance() = ForgotConfirmFragment()
    }

    private lateinit var binding: FragmentAuthForgotConfirmBinding

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthForgotConfirmBinding.inflate(inflater, container, false)
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
            processData("token", it)
        })
        viewModel.resendForgotToken.observe(viewLifecycleOwner, {
            processData("code", it)
        })

        // Setup listener
        binding.textOtp.requestFocus()
        binding.textOtp.setOnEditorActionListener { _, actionId, _ ->
            if( actionId == EditorInfo.IME_ACTION_DONE ) {
                Timber.tag("B").d("Done clicked")
                binding.btnSend.performClick()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        binding.btnSend.setOnClickListener {
            doConfirm()
        }
        binding.textResendCode.setOnClickListener {
            sessionManager.forgotEmail?.let { it1 -> viewModel.resendForgotCode(it1) }
        }
    }

    override fun processResult(tag: String, data: Any?) {
        super.processResult(tag, data)
        if( tag == "token" ) {
            // If confirm load confirm password
            showChangePassword()
        }

        if( tag == "code" ) {
            // If resend
            activity?.let {
                val alert = AlertDialog.Builder(it, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                    .setTitle("Berhasil")
                    .setPositiveButton("Ok") { _, _ ->
                    }
                    .create()

                alert.setMessage("Kode verifikasi telah dikirimkan ke email Anda.")
                alert.show()
            }
        }
    }

    @InternalCoroutinesApi
    private fun doConfirm() {
        if( binding.textOtp.isNotNullAndMinLength(
                4,
                "Harap isi terlebih dahulu",
                "Harap isi terlebih dahulu"
            )
        ) {
            sessionManager.forgotToken?.let {
                viewModel.forgotConfirm(
                    binding.textOtp.text.toString().trim(),
                    it
                )
            }
        }
    }

    private fun showChangePassword() {
        val action = ForgotConfirmFragmentDirections.actionForgotConfirmFragmentToForgotChangePasswordFragment()
        findNavController().navigate(action)
    }
}