package com.havelisolutions.videocaller.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.havelisolutions.videocaller.R
import com.havelisolutions.videocaller.databinding.RegisterationBinding
import com.havelisolutions.videocaller.extensions.*
import com.havelisolutions.videocaller.utils.ActivityNavigator
import java.util.concurrent.TimeUnit

class RegisterationActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }
    lateinit var binding: RegisterationBinding
    private var checker = "";
    private var phoneNumber = "";
    private var mVerificationId = ""
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registeration)
        init()
        setListeners()
    }

    override fun onStart() {
        super.onStart()
        val user=FirebaseAuth.getInstance().currentUser
        user?.let {
            ActivityNavigator<ContactsActivity>(this,ContactsActivity::class.java)
        }
    }
    private fun init() {

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showErrorMessage(p0.message.toString())
                binding.run {
                    phoneAuth.show()
                    codeText.show()
                    progressBar.hide()
                }

            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                mVerificationId=p0
                mResendToken=p1
                binding.phoneAuth.hide()
                checker="Code Sent"
                binding.run {
                    continueNextButton.text="Submit"
                    codeText.show()
                    progressBar.hide()
                }
                showInfoMessage("Code has been sent please check and write")
            }
        }
        mAuth=FirebaseAuth.getInstance()
    }

    public fun showHideProgressBar(progressBar: ProgressBar?) {
        progressBar?.let { progressBar ->
            if (progressBar.isShown) {
                progressBar.hide()
                return@let
            }
            progressBar.show()
        }

    }

    private fun setListeners() {
        binding.run {
            ccp.registerCarrierNumberEditText(binding.phoneText)
            continueNextButton.setOnClickListener {

                if (continueNextButton.text.equals("Submit") || checker.equals("Code Sent")) {
                    if(phoneText.text.toString().isEmpty()){
                        showErrorMessage("Please write phone number")
                    }else{
                        showHideProgressBar(binding.progressBar)
                        signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(mVerificationId,phoneText.text.toString()))
                    }
                } else {
                    phoneNumber = ccp.fullNumberWithPlus
                    phoneNumber.let {
                        if (!it.equals("")) {
                            showHideProgressBar(progressBar)
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber, // Phone number to verify
                                60, // Timeout duration
                                TimeUnit.SECONDS, // Unit of timeout
                                this@RegisterationActivity, // Activity (for callback binding)
                                callbacks
                            ) // OnVerificationStateChangedCallbacks
                        } else
                            showInfoMessage("Please write valid phone number")
                    }
                }
            }

        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    showHideProgressBar(binding.progressBar)
                    showSuccessMessage("Congos you are logged in")
                    ActivityNavigator<ContactsActivity>(this,ContactsActivity::class.java)
                    val user = task.result?.user
                    // ...
                } else {
                    // Sign in failed, display a message and update the UI
                    showErrorMessage("Logged in falided ${task.exception.toString()}")
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }
}
