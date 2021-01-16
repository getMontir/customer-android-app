package com.getmontir.customer.view.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.getmontir.customer.R
import com.getmontir.customer.databinding.FragmentAuthRegisterBinding
import com.getmontir.customer.view.ui.MainActivity
import com.getmontir.customer.view.ui.base.GetFragment
import com.getmontir.customer.viewmodel.AuthViewModel
import com.getmontir.lib.data.response.ApiErrorValidation
import com.getmontir.lib.ext.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RegisterFragment.
         */
        @JvmStatic
        fun newInstance() = RegisterFragment()

        private val TAG = RegisterFragment::class.java.simpleName
    }

    private lateinit var binding: FragmentAuthRegisterBinding

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var callbackManager: CallbackManager

    private val viewModel: AuthViewModel by viewModel()

    private val firebaseAuth: FirebaseAuth by inject()

    private val googleSignInClient: GoogleSignInClient by inject()

    private var fcmToken: String = ""

    private var idToken: String? = null

    private var idEmail: String? = null

    private var account: GoogleSignInAccount? = null

    @InternalCoroutinesApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Setup Activity Result Launcher
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if( result.resultCode == Activity.RESULT_OK ) {
                val data: Intent? = result.data

                val task = GoogleSignIn.getSignedInAccountFromIntent(data)

                account = task.getResult(ApiException::class.java)
                Timber.tag(TAG).d("Account $account")

                idToken = account?.idToken
                Timber.tag(TAG).d("Token $idToken")

                idEmail = account?.email

                idToken?.let {
                    viewModel.registerGoogle(it, fcmToken)
                }
            }
        }

        binding = FragmentAuthRegisterBinding.inflate( inflater, container, false )
        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Toolbar
        val toolbar: Toolbar = binding.toolbar
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment)

        // Setup Firebase
        setupFCM()
        callbackManager = CallbackManager.Factory.create()

        // Setup View Model
        viewModel.token.observe( viewLifecycleOwner, {
            processData("token", it)
        })
        viewModel.user.observe(viewLifecycleOwner, {
            processData("user", it)
        })

        // Setup Listener
        binding.btnSignUp.setOnClickListener {
            doRegister()
        }
        binding.divider.btnSocialGoogle.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            resultLauncher.launch(intent)
        }
        binding.divider.btnSocialFacebook.setOnClickListener {
            binding.divider.btnFacebook.performClick()
        }

        binding.divider.btnFacebook.setPermissions("email", "public_profile")
        binding.divider.btnFacebook.fragment = this
        binding.divider.btnFacebook.registerCallback(callbackManager, object: FacebookCallback<LoginResult> {

            override fun onSuccess(result: LoginResult?) {
                val accessToken = result?.accessToken
                val token = accessToken?.token
                Timber.tag(TAG).d("facebook:onSuccess $token")
                if (token != null) {
                    viewModel.registerFacebook(token, fcmToken)
                }
            }

            override fun onCancel() {
                Timber.tag(TAG).d("facebook:onCancel")
            }

            override fun onError(error: FacebookException?) {
                Timber.tag(TAG).d("facebook:onError")
                Timber.tag(TAG).e(error)
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Timber.tag(TAG).d("ACTIVITYRESULT:RESULTCODE:: $resultCode")
        Timber.tag(TAG).d("ACTIVITYRESULT:REQUESTCODE:: $requestCode")

        if( resultCode == Activity.RESULT_OK ) {
            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setupFCM() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }

                // Get new Instance ID token
                fcmToken = task.result.toString()
                Timber.tag(TAG).d("FCM Token: $fcmToken")
            }
    }

    @InternalCoroutinesApi
    private fun doRegister() {
        val name = binding.textInputName.text.toString().trim()
        val phone = binding.textInputPhone.text.toString().trim()
        val email = binding.textInputEmail.text.toString().trim()
        val password = binding.textInputPassword.text.toString().trim()
        val passwordConfirmation = binding.textInputPasswordRepeat.text.toString().trim()

        resources.apply {
            if(
                binding.textInputName.isNotNullOrEmpty(
                    getString(R.string.error_field_name_empty)
                )
                && binding.textInputPhone.isPhoneNotNull(
                    getString(R.string.error_field_phone_empty),
                    getString(R.string.error_field_phone_length)
                )
                && binding.textInputEmail.isEmailNotNull(
                    getString(R.string.error_field_email_empty),
                    getString(R.string.error_field_email_invalid)
                )
                && binding.textInputPassword.isPasswordConfirmation(
                    binding.textInputPasswordRepeat,
                    getString(R.string.error_field_password_empty),
                    getString(R.string.error_field_password_confirmation_empty),
                    getString(R.string.error_field_password_length),
                    getString(R.string.error_field_password_confirmation)
                )
                && binding.textInputAgree.isMustChecked(
                    getString(R.string.error_field_agree_unchecked)
                )
            ) {
                viewModel.register( name, phone, email, password, passwordConfirmation )
            }
        }
    }

    @InternalCoroutinesApi
    override fun processResult(tag: String, data: Any?) {
        super.processResult(tag, data)

        if( tag == "token" ) {
            // Load user information
            viewModel.profile()
        }

        if( tag == "user" ) {
            // Launch main activity
            startActivity(
                Intent(
                    requireContext(), MainActivity::class.java
                )
            )
            activity?.finish()
        }
    }

    override fun handleHttpValidation(tag: String, e: Exception, data: ApiErrorValidation?) {
        super.handleHttpValidation(tag, e, data)
        if( tag == "token" ) {
            if( data != null ) {
                if( data.errors.name != null ) {
                    binding.textLayoutName.error = data.errors.name!!.joinToString {
                        it + "\n"
                    }
                    binding.textInputName.requestFocus()
                }
                if( data.errors.phone != null ) {
                    binding.textLayoutPhone.error = data.errors.phone!!.joinToString {
                        it + "\n"
                    }
                    binding.textInputPhone.requestFocus()
                }
                if( data.errors.email != null ) {
                    binding.textLayoutEmail.error = data.errors.email!!.joinToString {
                        it + "\n"
                    }
                    binding.textInputEmail.requestFocus()
                }
                if( data.errors.password != null ) {
                    binding.textLayoutPassword.error = data.errors.password!!.joinToString {
                        it + "\n"
                    }
                    binding.textInputPassword.requestFocus()
                }
            }
        }
    }
}