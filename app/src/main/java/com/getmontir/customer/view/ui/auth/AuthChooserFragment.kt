package com.getmontir.customer.view.ui.auth

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.viewpager.widget.ViewPager
import com.getmontir.customer.R
import com.getmontir.customer.databinding.FragmentAuthChooserBinding
import com.getmontir.customer.view.adapter.AuthChooserAdapter
import com.getmontir.customer.view.ui.base.GetFragment

/**
 * A simple [Fragment] subclass.
 * Use the [AuthChooserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthChooserFragment : GetFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AuthChooserFragment.
         */
        @JvmStatic
        fun newInstance() = AuthChooserFragment()
    }

    private lateinit var binding: FragmentAuthChooserBinding

    private lateinit var sliderAdapter: AuthChooserAdapter

    private var layouts: IntArray = intArrayOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // View pager lists
        layouts = intArrayOf(
            R.layout.item_walkthrough_01,
            R.layout.item_walkthrough_02,
            R.layout.item_walkthrough_03
        )

        // Add first dots
        addSliderDots(0)

        // Set Adapter
        sliderAdapter = AuthChooserAdapter(requireContext(), layouts)
        binding.viewPager.adapter = sliderAdapter

        // Set Listener
        binding.viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) = onSliderChange(position)
            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        binding.btnLogin.setOnClickListener {
            val action = AuthChooserFragmentDirections.actionAuthChooserFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        binding.btnRegister.setOnClickListener {  }

        val toolbar: Toolbar = binding.toolbar
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.authChooserFragment,
            R.id.loginFragment
        ))
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment, appBarConfiguration)
    }

    private fun onSliderChange(position: Int) {
        addSliderDots(position)
    }

    private fun addSliderDots(currentIndex: Int) {
        val dots = arrayOfNulls<TextView>(layouts.size)

        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        binding.layoutDots.removeAllViews()

        for (i in dots.indices) {
            dots[i] = TextView(context)
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ) {
                dots[i]?.text = Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY)
            } else {
                dots[i]?.text = Html.fromHtml("&#8226;")
            }
            dots[i]?.textSize = 35f
            dots[i]?.setTextColor(colorsInactive[currentIndex])
            binding.layoutDots.addView(dots[i])
        }

        if (dots.isNotEmpty())
            dots[currentIndex]?.setTextColor(colorsActive[currentIndex])
    }

    private fun getItem(n: Int): Int {
        return binding.viewPager.currentItem + n
    }
}