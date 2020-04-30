package com.havelisolutions.videocaller.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.havelisolutions.videocaller.R
import com.havelisolutions.videocaller.databinding.RegisterationBinding
import com.havelisolutions.videocaller.extensions.showInfoMessage

class RegisterationActivity : AppCompatActivity() {
    lateinit var binding:RegisterationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_registeration)
        setListeners()
    }
    private fun setListeners(){
        binding.continueNextButton.setOnClickListener {
            showInfoMessage("Button clicked")
        }
    }
}
