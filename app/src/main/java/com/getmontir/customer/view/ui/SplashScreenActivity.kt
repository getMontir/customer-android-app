package com.getmontir.customer.view.ui

import android.os.Bundle
import com.getmontir.customer.R
import com.getmontir.customer.databinding.ActivitySplashScreenBinding
import com.getmontir.customer.view.ui.splash.SplashScreenFragment
import com.getmontir.lib.presentation.base.BaseActivity

open class SplashScreenActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}