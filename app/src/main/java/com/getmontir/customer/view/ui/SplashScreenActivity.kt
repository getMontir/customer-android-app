package com.getmontir.customer.view.ui

import android.os.Bundle
import com.getmontir.customer.R
import com.getmontir.customer.view.ui.splash.SplashScreenFragment
import com.getmontir.lib.presentation.base.BaseActivity

class SplashScreenActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SplashScreenFragment.newInstance())
                .commitNow()
        }
    }
}