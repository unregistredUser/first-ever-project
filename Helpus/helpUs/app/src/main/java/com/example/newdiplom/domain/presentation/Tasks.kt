package com.example.newdiplom.domain.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.newdiplom.R

class Tasks : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        //init

        val taskListIntent = Intent(this, Tasklist::class.java)
        val householdImage = findViewById<LinearLayout>(R.id.linearLayout1)
        val buyImage = findViewById<LinearLayout>(R.id.linearLayout2)
        val helpImage = findViewById<LinearLayout>(R.id.linearLayout4)
        val deliveryImage = findViewById<LinearLayout>(R.id.linearLayout3)
        val buttSearchTask = findViewById<ImageView>(R.id.searchtask)
        val buttToProfile = findViewById<ImageView>(R.id.profiled)
        val arguments = intent.extras
        val idUser = arguments!!["uid"].toString()


        buttToProfile.setOnClickListener {
            val profileVoluntIntent = Intent(this, profilevolunteer::class.java)
            profileVoluntIntent.putExtra("uid", idUser)
            startActivity(profileVoluntIntent)
            overridePendingTransition(1, 1)
        }

        buttSearchTask.setOnClickListener {
            val taskIntent = Intent(this, Tasks::class.java)
            taskIntent.putExtra("idus", idUser)
            startActivity(taskIntent)
            overridePendingTransition(1, 1)
        }

        //init

        householdImage.setOnClickListener {
            taskListIntent.putExtra("type", "Работа по дому")
            Toast.makeText(this, "Работа по дому", Toast.LENGTH_SHORT).show()
            taskListIntent.putExtra("userid", idUser)
            startActivity(taskListIntent)
            overridePendingTransition(1, 1)

        }
        buyImage.setOnClickListener {
            taskListIntent.putExtra("type", "Покупки")
            Toast.makeText(this, "Покупки", Toast.LENGTH_SHORT).show()
            taskListIntent.putExtra("userid", idUser)
            startActivity(taskListIntent)
            overridePendingTransition(1, 1)

        }
        helpImage.setOnClickListener {
            taskListIntent.putExtra("type", "Доставка")
            Toast.makeText(this, "Доставка", Toast.LENGTH_SHORT).show()
            taskListIntent.putExtra("userid", idUser)
            startActivity(taskListIntent)
            overridePendingTransition(1, 1)

        }
        deliveryImage.setOnClickListener {
            taskListIntent.putExtra("type", "Сопровождение")
            Toast.makeText(this, "Сопровождение", Toast.LENGTH_SHORT).show()
            taskListIntent.putExtra("userid", idUser)
            startActivity(taskListIntent)
            overridePendingTransition(1, 1)

        }
    }


}