package com.example.newdiplom.domain.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.newdiplom.R

class Roles : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roles)
        Handler().postDelayed(
            {
                @Override
                fun Run() {
                    val chooseRolesIntent = Intent(this, chooseRoles::class.java)
                    startActivity(chooseRolesIntent)
                    finish()
                }
                Run()
            },
            3000,
        )

    }

}