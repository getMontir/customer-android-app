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
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.getmontir.customer.R
import com.getmontir.customer.databinding.FragmentAuthLoginBinding
import com.getmontir.customer.view.ui.base.GetFragment
import com.getmontir.customer.viewmodel.LoginViewModel
import com.getmontir.lib.ext.isEmailNotNull
import com.getmontir.lib.ext.isNotNullOrEmpty
import com.getmontir.lib.ext.isPassword
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
        private val TAG = LoginFragment::class.java.simpleName
    }

    private lateinit var binding: FragmentAuthLoginBinding

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var callbackManager: CallbackManager

    private val viewModel: LoginViewModel by viewModel()

    private val firebaseAuth: FirebaseAuth by inject()

    private val googleSignInClient: GoogleSignInClient by inject()

    private var fcmToken: String = ""

    private var idToken: String? = null

    private var idEmail: String? = null

    private var account: GoogleSignInAccount? = null

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

                idToken?.let { viewModel.loginGoogle(it, fcmToken ) }
            }
        }

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

        // Setup FCM
        setupFCM()
        callbackManager = CallbackManager.Factory.create()

        // Setup view model
        viewModel.token.observe(viewLifecycleOwner, {
            processData("token", it)
        })
        viewModel.user.observe(viewLifecycleOwner, {
            processData("user", it)
        })

        // Setup listener
        binding.btnSignIn.setOnClickListener {
            doLogin()
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
                    viewModel.loginFacebook(token, fcmToken)
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
        binding.textActionForgot.setOnClickListener {  }
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
            activity?.finish()
        }
    }

    @InternalCoroutinesApi
    private fun doLogin() {
        val email = binding.textInputEmail.text.toString().trim()
        val password = binding.textInputPassword.text.toString().trim()

        if(
            binding.textInputEmail.isEmailNotNull("Harap isi email Anda", "Harap isi email yang valid")
            && binding.textInputPassword.isPassword("Harap isi kata sandi Anda", "Kata sandi minimal 6 karakter")
        ) {
            binding.textLayoutEmail.error = null
            binding.textLayoutPassword.error = null
            viewModel.login(email, password)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.tag(TAG).d("ACTIVITYRESULT:RESULTCODE:: $resultCode")
        Timber.tag(TAG).d("ACTIVITYRESULT:REQUESTCODE:: $requestCode")

        if( resultCode == Activity.RESULT_OK ) {
            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * When user credential not found
     */
    override fun handleHttpNotFound(tag: String, e: Exception) {
        if( tag == "token" ) {
            binding.textLayoutEmail.error = "Pengguna tidak ditemukan"
        }
    }

    /**
     * When user credential wrong
     */
    override fun handleHttpValidation(tag: String, e: Exception) {
        if( tag == "token" ) {
            binding.textLayoutEmail.error = "Email atau kata sandi salah"
        }
    }

    /**
     * When user is banned
     */
    override fun handleHttpBadRequest(tag: String, e: Exception) {
        if( tag == "token" ) {
            binding.textLayoutEmail.error = "Akun Anda dalam status diblokir"
        }
    }
}