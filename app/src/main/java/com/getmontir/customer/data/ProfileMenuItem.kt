package com.getmontir.customer.data

import androidx.annotation.DrawableRes

data class ProfileMenuItem(
    val id: String,
    val title: String,
    @DrawableRes val icon: Int
)
