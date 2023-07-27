package com.example.newdiplom.domain.presentation

import androidx.appcompat.app.AppCompatActivity
import android.content.ContentValues
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import com.example.newdiplom.R
import com.example.newdiplom.domain.entities.fireBaseClasses.NewResponse
import com.example.newdiplom.domain.entities.networkChangeListener.NetworkChangeListener
import com.google.firebase.database.FirebaseDatabase


class AcceptTask : AppCompatActivity() {
    val networkChangeListener =
        NetworkChangeListener()
    val tasks = "Tasks"

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept_task)

        //initElems
        val provileVoluntIntent = Intent(this, profilevolunteer::class.java)
        val arguments = intent.extras
        val idTask = arguments!!["taskid"].toString()
        val idUser = arguments!!["idUser"].toString()
        val responceId = idTask + "1"
        val nameTask = arguments!!["heading1"].toString()
        val dateTaskArgs = arguments!!["heading2"].toString()
        val typeTask = arguments!!["typeTask"].toString()
        val imgId = arguments!!["image"]
        val nametask = findViewById<TextView>(R.id.nametask)
        val taskCategory = findViewById<TextView>(R.id.cattask)
        val dateTask = findViewById<TextView>(R.id.datetask)
        val imgIdtask = findViewById<ImageView>(R.id.catimage)
        val progress = findViewById<ProgressBar>(R.id.Bar)
        val butAcceptTask = findViewById<Button>(R.id.accepted)
        val toMyResponcesButt = findViewById<ImageView>(R.id.myaccepted)
        val toProfileVoluntButt = findViewById<ImageView>(R.id.profiled)
        //initElems

        //hider

        progress.visibility = View.VISIBLE
        dateTask.visibility = View.GONE
        imgIdtask.visibility = View.GONE
        nametask.visibility = View.GONE
        taskCategory.visibility = View.GONE

        //hider
        toMyResponcesButt.setOnClickListener {
            val myResponcesIntent = Intent(this, ResponcesVolunteer::class.java)
            myResponcesIntent.putExtra("uid", idUser)
            startActivity(myResponcesIntent)
            overridePendingTransition(1, 1)
        }
        toProfileVoluntButt.setOnClickListener {
            provileVoluntIntent.putExtra("uid", idUser)
            startActivity(provileVoluntIntent)
            overridePendingTransition(1, 1)
        }
        fun initElems() {
            nametask.text = nameTask
            dateTask.text = dateTaskArgs
            taskCategory.text = typeTask
            imgIdtask.setImageResource(imgId as Int)
            progress.visibility = View.GONE
            taskCategory.visibility = View.VISIBLE
            dateTask.visibility = View.VISIBLE
            imgIdtask.visibility = View.VISIBLE
            nametask.visibility = View.VISIBLE
        }

        initElems()

        butAcceptTask.setOnClickListener {
            declineinfo(idTask)

            var newResponse =
                NewResponse(
                    responceId,
                    idTask,
                    idUser,
                    dateTaskArgs,
                    typeTask,
                    nameTask
                )

            FirebaseDatabase.getInstance().getReference().child("/Responces/" + idUser)
                .setValue(newResponse)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "Задача успешно принята",
                            Toast.LENGTH_SHORT
                        ).show()
                        provileVoluntIntent.putExtra("uid", idUser)
                        startActivity(provileVoluntIntent)

                    } else {

                        Log.w(ContentValues.TAG, "createVolunteerInfo:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Ошибка, задача не принята",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
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

    private fun declineinfo(id: String) {
        val dataBaseReference = FirebaseDatabase.getInstance().getReference("Tasks").child(id)
        dataBaseReference.removeValue().addOnSuccessListener {

        }.addOnFailureListener {
            Toast.makeText(this, "Ошибка, попробуйте еше раз", Toast.LENGTH_SHORT).show()
        }

    }
}