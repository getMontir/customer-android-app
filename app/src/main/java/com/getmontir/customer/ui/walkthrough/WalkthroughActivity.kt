package com.getmontir.customer.ui.walkthrough

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.getmontir.customer.R
import com.getmontir.customer.databinding.ActivityWalkthroughBinding

class WalkthroughActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWalkthroughBinding

    private lateinit var sliderAdapter: WalkthroughAdapter

    private var layouts: IntArray = intArrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkthroughBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // View pager lists
        layouts = intArrayOf(
            R.layout.walkthrough_01,
            R.layout.walkthrough_02,
            R.layout.walkthrough_03
        )

        // Add first dots
        addSliderDots(0)

        // Set adapter
        sliderAdapter = WalkthroughAdapter(this, layouts)
        binding.viewPager.adapter = sliderAdapter

        // Set listener
        binding.btnWalkthroughNext.setOnClickListener {
            onNextButtonClicked()
        }
        binding.btnWalkthroughSkip.setOnClickListener {
            onSkipButtonClicked()
        }
        binding.viewPager.addOnPageChangeListener(object:ViewPager.OnPageChangeListener {
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
    }

    private fun onSkipButtonClicked() {
        // Set session is used
        // sessionManager.isUsed = true

        // Launch acitivity
//        startActivity(
//            Intent(this, )
//        )
        finish()
    }

    private fun onNextButtonClicked() {
        val current = getItem(+1)
        if (current < layouts.size) {
            // move to next screen
            binding.viewPager.currentItem = current
        } else {
            onSkipButtonClicked()
        }
    }

    private fun onSliderChange(position: Int) {
        addSliderDots(position)

        // changing the next button text 'NEXT' / 'GOT IT'
        if (position == layouts.size - 1) {
            // last page. make button text to GOT IT
            binding.btnWalkthroughNext.text = getString(R.string.walkthrough_button_finish)
            binding.btnWalkthroughSkip.visibility = View.GONE

        } else {

            // still pages are left
            binding.btnWalkthroughNext.text = getString(R.string.walkthrough_button_next)
            binding.btnWalkthroughSkip.visibility = View.VISIBLE
        }
    }

    private fun addSliderDots(currentIndex: Int) {
        val dots = arrayOfNulls<TextView>(layouts.size)

        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        binding.layoutDots.removeAllViews()

        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]?.text = Html.fromHtml("&#8226;")
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