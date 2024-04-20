package com.example.balo.ui.register

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.balo.data.model.UserEntity
import com.example.balo.data.model.enum.Collection
import com.example.balo.data.model.enum.User
import com.example.balo.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.firestore
import java.util.concurrent.TimeUnit


class RegisterViewModel : ViewModel() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private var otp = ""

    fun registerAccount(
        activity: Activity,
        phone: String,
        handleExistPhone: () -> Unit,
        handleVerify: () -> Unit,
        handleFail: () -> Unit,
        handleSend: () -> Unit
    ) {
        isPhoneRegister(phone = phone, isRegister = { isExist ->
            if (isExist) {
                handleExistPhone.invoke()
            } else {
//                sendOtp(activity = activity, phoneNumber = phone, handleCompleted = {
//                    handleVerify.invoke()
//                }, handleFail = { handleFail.invoke() },
//                    handleSend = { handleSend.invoke() })
                handleVerify.invoke()
            }
        }, isError = { handleFail.invoke() })
    }


    fun createUser(user: UserEntity, handleSuccess: () -> Unit, handleFail: (String) -> Unit) {
        val data = hashMapOf(
            User.NAME.property to user.username,
            User.PHONE.property to user.phone,
            User.PASSWORD.property to Utils.hashPassword(user.password),
            User.ROLE.property to user.role
        )
        db.collection(Collection.USER.collectionName).add(data)
            .addOnSuccessListener {
                handleSuccess.invoke()
            }
            .addOnFailureListener { e ->
                handleFail.invoke(e.message.toString())
            }
    }

    fun isPhoneRegister(phone: String, isRegister: (Boolean) -> Unit, isError: () -> Unit) {
        db.collection(Collection.USER.collectionName).whereEqualTo(User.PHONE.property, phone).get()
            .addOnSuccessListener { querySnapshot ->
                val phoneExists = !querySnapshot.isEmpty
                isRegister.invoke(phoneExists)
            }
            .addOnFailureListener {
                isError.invoke()
            }
    }

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
                    Log.w("VANVAN", "signInWithCredential:failure", task.exception)
                    handleFail.invoke()
                }
            }
    }

}