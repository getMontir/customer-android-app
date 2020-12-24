package com.getmontir.customer.ui.auth.ui.auth

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.ActivityNavigator
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.getmontir.customer.R
import com.getmontir.customer.databinding.AuthFragmentBinding
import com.getmontir.customer.ui.auth.ui.forgot.ForgotPasswordActivity
import com.getmontir.customer.viewmodel.SignInViewModel
import com.getmontir.lib.presentation.base.BaseFragment
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

open class SignInFragment : BaseFragment() {

    private lateinit var binding: AuthFragmentBinding

    companion object {
        fun newInstance() = SignInFragment()
    }

    private val viewModel: SignInViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = AuthFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @InternalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupView()

//        viewModel.provinces.observe( viewLifecycleOwner, {
//            processData("province", it)
//        })
//        viewModel.loadProvince()
    }

    private fun setupView() {
        val spannable = SpannableString("Belum memiliki akun? Daftar")
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
//                findNavController().navigate(action)
                activity?.let { Navigation.findNavController(it, R.id.container).navigate(action) }
            }
        }
        spannable.setSpan(
            clickableSpan,
            21,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            spannable.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.secondaryColor, context?.theme)),
                21,
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
        binding.txtActionRegister.movementMethod = LinkMovementMethod()
        binding.txtActionRegister.text = spannable
        binding.txtActionRegister.linksClickable = true
        binding.txtActionRegister.isClickable = true

        binding.textActionForgot.setOnClickListener {
//            val action = SignInFragmentDirections.actionSignInFragmentToNavAuthForgot()
//            findNavController().navigate(action)

            context?.let { c ->
                startActivity(
                    Intent(c, ForgotPasswordActivity::class.java)
                )
            }

//            context?.let { it1 ->
//                val destination = ActivityNavigator(it1).createDestination().setIntent(Intent(it1, ForgotPasswordActivity::class.java))
//                ActivityNavigator(it1).navigate(destination, null, null, null)
//            }
        }

        binding.divider.dividerText = "atau masuk dengan"
        binding.divider.btnSocialGoogle.setOnClickListener {  }
        binding.divider.btnSocialFacebook.setOnClickListener {  }
    }

}