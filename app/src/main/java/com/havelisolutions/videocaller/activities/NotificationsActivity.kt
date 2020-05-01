package com.havelisolutions.videocaller.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.havelisolutions.videocaller.R
import com.havelisolutions.videocaller.databinding.NotificationsBinding
import java.io.IOException

class NotificationsActivity : AppCompatActivity() {
    lateinit var binding: NotificationsBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_notifications)
    }


}
