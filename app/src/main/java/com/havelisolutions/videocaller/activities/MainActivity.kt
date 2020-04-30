package com.havelisolutions.videocaller.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.havelisolutions.videocaller.R
import com.havelisolutions.videocaller.databinding.MainBinding
import com.havelisolutions.videocaller.utils.ActivityNavigator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainBinding
    private lateinit var bottomNavigationListener: BottomNavigationView.OnNavigationItemReselectedListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
        setListeners()
    }

    private fun init() {
        bottomNavigationListener = BottomNavigationView.OnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    ActivityNavigator<MainActivity>(this,MainActivity::class.java)
                }
                R.id.navigation_settings -> {
                    ActivityNavigator<SettingsActivity>(this,SettingsActivity::class.java)

                }
                R.id.navigation_notifications -> {
                    ActivityNavigator<NotificationsActivity>(this,NotificationsActivity::class.java)

                }
                R.id.navigation_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    ActivityNavigator<RegisterationActivity>(this,RegisterationActivity::class.java)

                    finish()
                }

            }
        }
    }

    private fun setListeners() {
        binding.run {
            navView.setOnNavigationItemReselectedListener(bottomNavigationListener)
        }
    }
}
