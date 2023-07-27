package com.example.newdiplom.domain.presentation

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.newdiplom.R
import com.example.newdiplom.domain.entities.fireBaseClasses.NewVolunteer
import com.example.newdiplom.domain.entities.networkChangeListener.NetworkChangeListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class profilevolunteer : AppCompatActivity() {
    private val USER_KEY = "Volunteer"
    val networkChangeListener =
        NetworkChangeListener()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilevolunteer)

        //init

        var voluntName = ""
        var voluntFullName = ""
        var voluntPhone = ""
        var counterForCheck = 2
        val voluntImageView = findViewById<ImageView>(R.id.avatarsvolunt)
        val voluntFamilView = findViewById<TextView>(R.id.namesecname3)
        val voluntPhoneView = findViewById<TextView>(R.id.phone2)
        val voluntPhoneNumView = findViewById<TextView>(R.id.textView8)
        val voluntContactData = findViewById<TextView>(R.id.datainfo3)
        val voluntAgeView = findViewById<TextView>(R.id.userage2)
        val voluntPreAgeView = findViewById<TextView>(R.id.preage)
        val buttToMyTask = findViewById<ImageView>(R.id.myaccepted)
        val buttToPreviousAct = findViewById<TextView>(R.id.exit3)
        val userTypeView = findViewById<TextView>(R.id.typeofvolunteer)
        val buttSearchTasks = findViewById<ImageView>(R.id.searchtask)
        val arguments = intent.extras
        val userId = arguments!!["uid"].toString()
        var voluntDatabaseRef = FirebaseDatabase.getInstance().getReference(USER_KEY)
        val progress = findViewById<ProgressBar>(R.id.progressBar3)
        val authIntent = Intent(this, Auth::class.java)

        //initEnd

        progress.visibility = View.VISIBLE
        userTypeView.visibility = View.GONE
        voluntPreAgeView.visibility = View.GONE
        voluntImageView.visibility = View.GONE
        voluntAgeView.visibility = View.GONE
        voluntContactData.visibility = View.GONE
        voluntFamilView.visibility = View.GONE
        voluntPhoneView.visibility = View.GONE
        voluntPhoneNumView.visibility = View.GONE
        buttToPreviousAct.visibility = View.GONE

        buttToPreviousAct.setOnClickListener {
            authIntent.putExtra("typed", "e")
            startActivity(authIntent)
            overridePendingTransition(1, 1)
        }



        buttSearchTasks.setOnClickListener {
            val taskIntent = Intent(this, Tasks::class.java)
            taskIntent.putExtra("uid", userId)
            startActivity(taskIntent)
            overridePendingTransition(1, 1)
        }
        buttToMyTask.setOnClickListener {
            val responseVoluntIntent = Intent(this, ResponcesVolunteer::class.java)
            responseVoluntIntent.putExtra("uid", userId)
            startActivity(responseVoluntIntent)
            overridePendingTransition(1, 1)
        }

        fun checkRole() {
            if (voluntAgeView.text.toString() == "50" && counterForCheck == 0) {

                authIntent.putExtra("typed", "e")
                authIntent.putExtra("CorrectRoleOrNot", "no")
                startActivity(authIntent)
                overridePendingTransition(1, 1)

            }
        }

        fun getVoluntInfoFromDb() {

            val vListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {


                    for (ds in dataSnapshot.children) {

                        val volunt: NewVolunteer = ds.getValue(
                            NewVolunteer::class.java)!!
                        if (volunt.iduser == userId) {
                            voluntName = (volunt.named.toString())
                            voluntFullName = (volunt.famil) + " " + voluntName
                            voluntPhone = (volunt.phonenum.toString())
                            voluntFamilView.text = voluntFullName
                            voluntPhoneView.text = voluntPhone
                            voluntAgeView.text = volunt.age.toString()
                            if (volunt.gender == "Мужчина") {
                                voluntImageView.setImageResource(R.drawable.profileman)
                            } else {
                                voluntImageView.setImageResource(R.drawable.profilewoman)
                            }

                            //show

                            progress.visibility = View.GONE
                            userTypeView.visibility = View.VISIBLE
                            voluntPreAgeView.visibility = View.VISIBLE
                            voluntContactData.visibility = View.VISIBLE
                            voluntImageView.visibility = View.VISIBLE
                            voluntFamilView.visibility = View.VISIBLE
                            voluntPhoneView.visibility = View.VISIBLE
                            voluntAgeView.visibility = View.VISIBLE
                            voluntPhoneNumView.visibility = View.VISIBLE
                            buttToPreviousAct.visibility = View.VISIBLE

                            //showEnd
                        } else {
                            counterForCheck = 0
                            checkRole()
                        }
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(ContentValues.TAG, "Databasereading:error", databaseError.toException())
                }
            }
            voluntDatabaseRef.addValueEventListener(vListener)
        }
        getVoluntInfoFromDb()


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