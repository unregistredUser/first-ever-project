package com.example.newdiplom.domain.presentation

import android.content.ContentValues
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.newdiplom.R
import com.example.newdiplom.domain.entities.fireBaseClasses.NewUser
import com.example.newdiplom.domain.entities.networkChangeListener.NetworkChangeListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class profile : AppCompatActivity() {


    val networkChangeListener =
        NetworkChangeListener()
    private val USER_KEY = "User"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //init

        var customerName = ""
        var fullCustomerName = ""
        var customerPhone = ""
        val authIntent = Intent(this, Auth::class.java)
        val profileImage = findViewById<ImageView>(R.id.avatars)
        val fullNameTextView = findViewById<TextView>(R.id.namesecname)
        val mobileNumView = findViewById<TextView>(R.id.phone)
        val preMobileNumView = findViewById<TextView>(R.id.textView10)
        val typeUserView = findViewById<TextView>(R.id.types)
        val buttToAddTaskAct = findViewById<ImageView>(R.id.addtask)
        val userContactDataView = findViewById<TextView>(R.id.datainfo)
        val userAgeView = findViewById<TextView>(R.id.userage)
        val toCallAct = findViewById<ImageView>(R.id.call)
        val userPreAgeView = findViewById<TextView>(R.id.beforeage)
        val buttToMyTaskAct = findViewById<ImageView>(R.id.mytasks)
        val buttToPreviousAct = findViewById<TextView>(R.id.exit3)
        val progress = findViewById<ProgressBar>(R.id.progressBar)
        var counterForCheck = 2
        val arguments = intent.extras
        val idUser = arguments!!["uid"].toString()
        val userDatabaseRef = FirebaseDatabase.getInstance().getReference(USER_KEY)
        //init

        buttToAddTaskAct.setOnClickListener {
            val addTaskIntent = Intent(this, add_task::class.java)
            addTaskIntent.putExtra("userid", idUser)
            startActivity(addTaskIntent)
            overridePendingTransition(1, 1)
        }
        buttToPreviousAct.setOnClickListener {
            val authIntent = Intent(this, Auth::class.java)
            authIntent.putExtra("typed", "z")
            startActivity(authIntent)
            overridePendingTransition(1, 1)
        }
        //hider
        buttToMyTaskAct.setOnClickListener {
            val myTaskIntent = Intent(this, mytasks::class.java)
            myTaskIntent.putExtra("uid", idUser)
            startActivity(myTaskIntent)
            overridePendingTransition(1, 1)
        }
        toCallAct.setOnClickListener {
            val intentResponse = Intent(this, responses::class.java)
            intentResponse.putExtra("uid", idUser)
            startActivity(intentResponse)
            overridePendingTransition(1, 1)
        }

        progress.visibility = View.VISIBLE
        buttToPreviousAct.visibility = View.GONE
        typeUserView.visibility = View.GONE
        userPreAgeView.visibility = View.GONE
        profileImage.visibility = View.GONE
        userAgeView.visibility = View.GONE
        userContactDataView.visibility = View.GONE
        fullNameTextView.visibility = View.GONE
        mobileNumView.visibility = View.GONE
        preMobileNumView.visibility = View.GONE

        //hider

        fun checkRole() {
            if (userAgeView.text.toString() == "50" && counterForCheck == 0) {

                authIntent.putExtra("typed", "z")
                authIntent.putExtra("CorrectRoleOrNot", "no")
                startActivity(authIntent)
                overridePendingTransition(1, 1)

            }
        }

        fun getUserInfoFromDb() {

            val vListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {


                    for (ds in dataSnapshot.children) {

                        val user: NewUser = ds.getValue(
                            NewUser::class.java
                        )!!
                        if (user.id == idUser) {
                            customerName = (user.name.toString())
                            fullCustomerName = (user.secname) + " " + customerName
                            customerPhone = (user.phone.toString())
                            fullNameTextView.text = fullCustomerName
                            mobileNumView.text = customerPhone
                            userAgeView.text = user.age.toString()
                            if (user.gender == "Мужчина") {
                                profileImage.setImageResource(R.drawable.profileman)
                            } else {
                                profileImage.setImageResource(R.drawable.profilewoman)
                            }
                            //show
                            progress.visibility = View.GONE
                            typeUserView.visibility = View.VISIBLE
                            buttToPreviousAct.visibility = View.VISIBLE
                            userPreAgeView.visibility = View.VISIBLE
                            userContactDataView.visibility = View.VISIBLE
                            profileImage.visibility = View.VISIBLE
                            fullNameTextView.visibility = View.VISIBLE
                            mobileNumView.visibility = View.VISIBLE
                            userAgeView.visibility = View.VISIBLE
                            preMobileNumView.visibility = View.VISIBLE
                            //show
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
            userDatabaseRef.addValueEventListener(vListener)

        }
        getUserInfoFromDb()

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