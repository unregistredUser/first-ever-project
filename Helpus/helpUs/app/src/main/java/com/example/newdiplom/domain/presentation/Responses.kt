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

class responses : AppCompatActivity(), MyAdapter.OnItemClickListener {
    val networkChangeListener =
        NetworkChangeListener()

    //preinit

    private val resKey = "Responces"
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<taskInf>
    lateinit var imageId: Array<Int>
    var listId = ArrayList<String>()
    var userId = ""

    //preinit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_responses)

        //init

        
        imageId = arrayOf(
            R.drawable.housework,
            R.drawable.deliverytask,
            R.drawable.helpgotask,
            R.drawable.shopify
        )
        newRecyclerView = findViewById(R.id.recyclertaskresponse)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        newRecyclerView.visibility = View.GONE
        val responseDatabaseRef = FirebaseDatabase.getInstance().getReference(resKey)
        val adapters = MyAdapter(newArrayList, this)
        val progress = findViewById<ProgressBar>(R.id.progressBar4)
        val buttAddTask = findViewById<ImageView>(R.id.addtask)
        val buttCallUser = findViewById<ImageView>(R.id.call)
        val buttToProfile = findViewById<ImageView>(R.id.toprofile)
        val arguments = intent.extras
        userId = arguments!!["uid"].toString()
        
        progress.visibility = View.VISIBLE

        //init

        buttCallUser.setOnClickListener {
            val responseIntent = Intent(this, responses::class.java)
            responseIntent.putExtra("uid", userId)
            startActivity(responseIntent)
            overridePendingTransition(1, 1)
        }

        buttToProfile.setOnClickListener {
            val profileIntent = Intent(this, profile::class.java)
            profileIntent.putExtra("uid", userId)
            startActivity(profileIntent)
            overridePendingTransition(1, 1)
        }

        buttAddTask.setOnClickListener {
            val addTaskIntent = Intent(this, add_task::class.java)
            addTaskIntent.putExtra("userId", userId)
            startActivity(addTaskIntent)
            overridePendingTransition(1, 1)
        }

        fun getResponseDataFromDb() {

            val vListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {


                    for (ds in dataSnapshot.children) {

                        val newResponse: NewResponse = ds.getValue(
                            NewResponse::class.java)!!
                        if (newResponse.idtask == userId) {
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
                                        val taskinf = taskInf(
                                            imageId[1],
                                            newResponse.taskname.toString(),
                                            newResponse.Date.toString()
                                        )
                                        newArrayList.add(taskinf)
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
            responseDatabaseRef.addValueEventListener(vListener)
            progress.visibility = View.GONE
            newRecyclerView.visibility = View.VISIBLE
        }
        getResponseDataFromDb()

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
        val profileForCallIntent = Intent(this, profileforcall::class.java)
        profileForCallIntent.putExtra("iduser", userId)
        profileForCallIntent.putExtra("heading1", clickedItem.nameTask)
        profileForCallIntent.putExtra("heading2", clickedItem.dateTask)
        startActivity(profileForCallIntent)
        overridePendingTransition(1, 1)
    }
}