package com.getmontir.customer.ui.auth.ui.auth

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.getmontir.customer.R
import com.getmontir.customer.databinding.SignUpFragmentBinding
import com.getmontir.customer.viewmodel.SignUpViewModel
import com.getmontir.lib.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

open class SignUpFragment : BaseFragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var binding: SignUpFragmentBinding

    private val viewModel: SignUpViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignUpFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        val spannable = SpannableString("Sudah mempunyai akun? Login")
        val start = 22
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController().popBackStack()
//                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
//                findNavController().navigate(action)
            }
        }
        spannable.setSpan(
            clickableSpan,
            22,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            spannable.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.secondaryColor, context?.theme)),
                22,
                spannable.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            spannable.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.secondaryColor)),
                21,
                spannable.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            21,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.txtActionLogin.movementMethod = LinkMovementMethod()
        binding.txtActionLogin.text = spannable
        binding.txtActionLogin.linksClickable = true
        binding.txtActionLogin.isClickable = true

        binding.divider.dividerText = "atau daftar dengan"
        binding.divider.btnSocialGoogle.setOnClickListener {  }
        binding.divider.btnSocialFacebook.setOnClickListener {  }
    }

}