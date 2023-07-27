package com.example.newdiplom.domain.presentation

import android.content.ContentValues
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newdiplom.domain.entities.customRecyclerAdapter.MyAdapter
import com.example.newdiplom.R
import com.example.newdiplom.domain.entities.fireBaseClasses.NewResponse
import com.example.newdiplom.domain.entities.networkChangeListener.NetworkChangeListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ResponcesVolunteer : AppCompatActivity(), MyAdapter.OnItemClickListener {

    val networkChangeListener =
        NetworkChangeListener()
    private val Reskey = "Responces"
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<taskInf>
    lateinit var imageId: Array<Int>
    var listId = ArrayList<String>()
    var userId = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_responces_volunteer)
        val arguments = intent.extras
        val adapters = MyAdapter(newArrayList, this)
        val progress = findViewById<ProgressBar>(R.id.ProgressBar10)
        val buttCall = findViewById<ImageView>(R.id.searchtask)
        val buttToProfile = findViewById<ImageView>(R.id.profiled)
        val responseFirebaseRef = FirebaseDatabase.getInstance().getReference(Reskey)

        imageId = arrayOf(
            R.drawable.housework,
            R.drawable.deliverytask,
            R.drawable.helpgotask,
            R.drawable.shopify
        )
        newRecyclerView = findViewById(R.id.recyclertaskresponse)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        newArrayList = arrayListOf<taskInf>()
        userId = arguments!!["uid"].toString()


        buttToProfile.setOnClickListener {
            val profileVoluntIntent = Intent(this, profilevolunteer::class.java)
            profileVoluntIntent.putExtra("uid", userId)
            startActivity(profileVoluntIntent)
            overridePendingTransition(1,1)
        }

        buttCall.setOnClickListener {
            val taskIntent = Intent(this, Tasks::class.java)
            taskIntent.putExtra("idus", userId)
            startActivity(taskIntent)
            overridePendingTransition(1,1)
        }


        fun getResponseInfoFromDb() {

            val vListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {


                    for (ds in dataSnapshot.children) {

                        val newResponse: NewResponse = ds.getValue(
                            NewResponse::class.java)!!
                        if (newResponse.idus == userId) {
                            if (newResponse.categ == "Работа по дому") {
                                val taskInf = taskInf(
                                    imageId[0],
                                    newResponse.taskname.toString(),
                                    newResponse.Date.toString()

                                )
                                newArrayList.add(taskInf)
                                listId.add(newResponse.idtask.toString())
                                newRecyclerView.adapter = adapters
                            } else {
                                if (newResponse.categ == "Покупки") {
                                    val taskInf = taskInf(
                                        imageId[3],
                                        newResponse.taskname.toString(),
                                        newResponse.Date.toString()
                                    )
                                    newArrayList.add(taskInf)
                                    listId.add(newResponse.idtask.toString())
                                    newRecyclerView.adapter = adapters
                                } else {
                                    if (newResponse.categ == "Доставка") {
                                        val taskInf = taskInf(
                                            imageId[1],
                                            newResponse.taskname.toString(),
                                            newResponse.Date.toString()
                                        )
                                        newArrayList.add(taskInf)
                                        listId.add(newResponse.idtask.toString())
                                        newRecyclerView.adapter = adapters
                                    } else {
                                        if (newResponse.categ == "Сопровождение") {
                                            val taskInf = taskInf(
                                                imageId[2],
                                                newResponse.taskname.toString(),
                                                newResponse.Date.toString()
                                            )
                                            newArrayList.add(taskInf)
                                            listId.add(newResponse.idtask.toString())
                                            newRecyclerView.adapter = adapters
                                        }
                                    }
                                }
                            }

                        }

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(ContentValues.TAG, "Databasereading:error", databaseError.toException())
                }
            }
            responseFirebaseRef.addValueEventListener(vListener)
            progress.visibility = View.GONE
            newRecyclerView.visibility = View.VISIBLE
        }
        getResponseInfoFromDb()


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

    override fun onItemClick(position: Int) {
        val clickedItem: taskInf = newArrayList[position]
        val profileCallZakIntent = Intent(this, profileforcallzak::class.java)
        profileCallZakIntent.putExtra("iduser", userId)
        profileCallZakIntent.putExtra("heading1", clickedItem.nameTask)
        profileCallZakIntent.putExtra("heading2", clickedItem.dateTask)
        startActivity(profileCallZakIntent)
        overridePendingTransition(1,1)
    }
}