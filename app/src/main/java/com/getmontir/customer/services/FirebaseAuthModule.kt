package com.getmontir.customer.services

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthModule {
    fun create(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}