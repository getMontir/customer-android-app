package com.getmontir.customer.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.getmontir.customer.R
import com.getmontir.customer.ui.auth.ui.auth.SignInFragment

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, SignInFragment.newInstance())
//                    .commitNow()
//        }
    }
}