package com.havelisolutions.videocaller.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.havelisolutions.videocaller.R
import com.havelisolutions.videocaller.databinding.MainBinding
import com.havelisolutions.videocaller.utils.ActivityNavigator

class ContactsActivity : AppCompatActivity() {
    private lateinit var binding: MainBinding
    private lateinit var bottomNavigationListener: BottomNavigationView.OnNavigationItemSelectedListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
        setListeners()
    }

    private fun init() {
        bottomNavigationListener = BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    ActivityNavigator(this,ContactsActivity::class.java)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_settings -> {
                    ActivityNavigator(this,SettingsActivity::class.java)
                    return@OnNavigationItemSelectedListener true

                }
                R.id.navigation_notifications -> {
                    ActivityNavigator(this,NotificationsActivity::class.java)
                    return@OnNavigationItemSelectedListener true


                }
                R.id.navigation_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    ActivityNavigator<RegisterationActivity>(this,RegisterationActivity::class.java)

                    finish()
                    return@OnNavigationItemSelectedListener true

                }


                else -> return@OnNavigationItemSelectedListener false
            }
        }
    }

    private fun setListeners() {
        binding.run {
            navView.setOnNavigationItemSelectedListener(bottomNavigationListener)
            findPeople.setOnClickListener {
                ActivityNavigator(this@ContactsActivity,FindPeopleActivity::class.java)
            }
        }
    }
    protected fun setRecycler(recyclerView: RecyclerView?) {
        recyclerView?.also {
            it.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
            // it.hasFixedSize()
//            adapter = genericAdapter
//            genericAdapter.itemList = list
        }
    }
}
