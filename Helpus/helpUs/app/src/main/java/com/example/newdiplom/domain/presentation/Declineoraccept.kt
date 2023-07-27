package com.example.newdiplom.domain.presentation

import android.content.ContentValues
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.newdiplom.*
import com.example.newdiplom.domain.entities.fireBaseClasses.NewResponse
import com.example.newdiplom.domain.entities.networkChangeListener.NetworkChangeListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class declineoraccept : AppCompatActivity() {

    //preInitStart

    val networkChangeListener =
        NetworkChangeListener()
    private val Reskey = "Responces"

    //preInitEnd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_declineoraccept)

        //initStart

        val mDatabasResponse = FirebaseDatabase.getInstance().getReference(Reskey)
        val catimage = findViewById<ImageView>(R.id.catimage)
        val catTaskText = findViewById<TextView>(R.id.cattask)
        val nameTaskText = findViewById<TextView>(R.id.nametask)
        val dateTaskText = findViewById<TextView>(R.id.datetask)
        val buttAccept = findViewById<Button>(R.id.galochka)
        val buttDecline = findViewById<Button>(R.id.krestik)
        val buttAddTask = findViewById<ImageView>(R.id.addtask)
        val buttCall = findViewById<ImageView>(R.id.call)
        val myTaskImage = findViewById<ImageView>(R.id.mytasks)
        val buttToProfileAct = findViewById<ImageView>(R.id.toprofile)
        val arguments = intent.extras
        val userId = arguments!!["userid"].toString()
        val nameTask = arguments!!["heading"].toString()
        val dateTask = arguments!!["heading2"].toString()
        val typeTask = arguments!!["type"].toString()
        val imgTask = arguments!!["image"]
        var idRes = ""

        //initEnd

        fun init() {
            nameTaskText.text = nameTask
            dateTaskText.text = dateTask
            catTaskText.text = typeTask
            catimage.setImageResource(imgTask as Int)
        }

        init()

        fun getResponceDataFromDB() {

            val vListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {


                    for (ds in dataSnapshot.children) {

                        val newResponse: NewResponse = ds.getValue(
                            NewResponse::class.java
                        )!!
                        if (newResponse.categ == nameTask && newResponse.taskname == dateTask) {
                            idRes = newResponse.idus.toString()
                        }

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(ContentValues.TAG, "Databasereading:error", databaseError.toException())
                }
            }
            mDatabasResponse.addValueEventListener(vListener)
        }
        getResponceDataFromDB()
        //init
        buttCall.setOnClickListener {
            val responceIntent = Intent(this, responses::class.java)
            responceIntent.putExtra("uid", userId)
            startActivity(responceIntent)
        }

        buttToProfileAct.setOnClickListener {
            val profileIntent = Intent(this, profile::class.java)
            profileIntent.putExtra("uid", userId)
            startActivity(profileIntent)
        }

        buttAddTask.setOnClickListener {
            val add_taskIntent = Intent(this, add_task::class.java)
            add_taskIntent.putExtra("userid", userId)
            startActivity(add_taskIntent)
        }
        buttAccept.setOnClickListener {
            acceptinfo(idRes)
            val myTaskIntent = Intent(this, mytasks::class.java)
            myTaskIntent.putExtra("uid", userId)
            startActivity(myTaskIntent)
        }
        buttDecline.setOnClickListener {
            declineinfo(idRes)
            val myTaskIntent = Intent(this, mytasks::class.java)
            myTaskIntent.putExtra("uid", userId)
            startActivity(myTaskIntent)
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

    private fun acceptinfo(id: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Responces").child(id)
        databaseRef.removeValue().addOnSuccessListener {
            Toast.makeText(this, "Задача выполнена ", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener(
            {
                Toast.makeText(this, "Ошибка, попробуйте еше раз", Toast.LENGTH_SHORT).show()
            }
        )

    }

    private fun declineinfo(id: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Responces").child(id)
        databaseRef.removeValue().addOnSuccessListener {
            Toast.makeText(this, "Задача выполнена ", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener(
            {
                Toast.makeText(this, "Ошибка, попробуйте еше раз", Toast.LENGTH_SHORT).show()
            }
        )

    }
}