package com.getmontir.customer.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.getmontir.customer.R
import com.getmontir.customer.presentation.ui.main.SplashFragment

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SplashFragment.newInstance())
                .commitNow()
        }
    }
}