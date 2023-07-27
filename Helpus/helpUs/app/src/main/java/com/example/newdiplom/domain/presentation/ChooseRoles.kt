package com.example.newdiplom.domain.presentation

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import com.example.newdiplom.R
import com.example.newdiplom.domain.entities.networkChangeListener.NetworkChangeListener

class chooseRoles : AppCompatActivity() {
    val networkChangeListener =
        NetworkChangeListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_roles)

        //init

        val buttVolunteer = findViewById<Button>(R.id.volonter)
        val buttCustomer = findViewById<Button>(R.id.zakazchik)
        val typeCustomer = "z"
        val typeVolunteer = "v"

        //init

        buttVolunteer.setOnClickListener {
            val authOrRegIntent = Intent(this, auth_or_reg::class.java)
            authOrRegIntent.putExtra("type", typeCustomer)
            startActivity(authOrRegIntent)
            overridePendingTransition(1, 1)

        }
        buttCustomer.setOnClickListener {
            val authOrRegIntent = Intent(this, auth_or_reg::class.java)
            authOrRegIntent.putExtra("type", typeVolunteer)
            startActivity(authOrRegIntent)
            overridePendingTransition(1, 1)

        }
    }

    override fun onStart() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeListener, filter)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(networkChangeListener)
        super.onStop()
    }
}