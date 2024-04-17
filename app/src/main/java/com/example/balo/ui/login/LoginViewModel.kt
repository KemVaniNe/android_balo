package com.example.balo.ui.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginViewModel : ViewModel() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var otp = ""

    fun sendOtp(
        activity: Activity,
        phoneNumber: String,
        handleCompleted: () -> Unit,
        handleFail: () -> Unit,
        handleSend: () -> Unit
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    handleCompleted.invoke()
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    handleFail.invoke()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    otp = p0
                    handleSend.invoke()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(otp: String, handleRightOTP: () -> Unit, handleFail: () -> Unit) {
        val credential = PhoneAuthProvider.getCredential(this.otp, otp)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    handleRightOTP.invoke()
                } else {
                    handleFail.invoke()
                }
            }
    }
}