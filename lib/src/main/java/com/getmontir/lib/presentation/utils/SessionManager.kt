package com.getmontir.lib.presentation.utils

import android.content.Context
import com.getmontir.lib.presentation.session

class SessionManager(private val context: Context, private val prefName: String) {

    init {
        PrefDelegate.init(context)
    }

    var isUsed: Boolean by booleanPref( prefName, session.isUsed, false)

    var language: String? by stringPref( prefName, session.language, "id")

    var isLoggedIn: Boolean by booleanPref( prefName, session.isLoggedIn, false)

}