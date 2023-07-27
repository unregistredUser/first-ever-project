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

class mytasks : AppCompatActivity(), MyAdapter.OnItemClickListener {
    
    val networkChangeListener =
        NetworkChangeListener()
    private val Reskey ="Responces"
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList <taskInf>
    lateinit var imageIdArray:Array<Int>
    var listId=ArrayList<String>()
    var listCateg=ArrayList<String>()
    var positionList = ArrayList<Int>()
    var userId=""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mytasks)

        //init

        val mDatabasResponse= FirebaseDatabase.getInstance().getReference(Reskey)
        imageIdArray= arrayOf(
            R.drawable.housework,
            R.drawable.deliverytask,
            R.drawable.helpgotask,
            R.drawable.shopify
        )
        newRecyclerView = findViewById(R.id.mytasklistrecycler)
        newRecyclerView.layoutManager= LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        newArrayList = arrayListOf<taskInf>()
        val adapters= MyAdapter(newArrayList,this)
        val progress = findViewById<ProgressBar>(R.id.progressBar6)
        val buttAddTask = findViewById<ImageView>(R.id.addtask)
        val buttToCallIntent = findViewById<ImageView>(R.id.call)
        val buttToProfile = findViewById<ImageView>(R.id.toprofile)
        val arguments = intent.extras
        userId = arguments!!["uid"].toString()
        var positionIterator=0
        //init
        progress.visibility = View.VISIBLE
        newRecyclerView.visibility= View.GONE

        buttToCallIntent.setOnClickListener{
            val responseIntent = Intent(this, responses::class.java)
            responseIntent.putExtra("uid",userId)
            startActivity(responseIntent)
            overridePendingTransition(1,1)
        }

        buttToProfile.setOnClickListener{
            val profileIntent = Intent(this, profile::class.java)
            profileIntent.putExtra("uid",userId)
            startActivity(profileIntent)
            overridePendingTransition(1,1)
        }

        buttAddTask.setOnClickListener{
            val add_taskIntent = Intent(this, add_task::class.java)
            add_taskIntent.putExtra("userid",userId)
            startActivity(add_taskIntent)
            overridePendingTransition(1,1)
        }
        fun getDataFromDB()
        {

            val vListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {


                    for (ds in dataSnapshot.children) {

                        val task: NewResponse = ds.getValue(
                            NewResponse::class.java)!!
                        if (task.idtask==userId) {
                            if (task.categ == "Работа по дому") {
                                val taskInfo = taskInf(
                                    imageIdArray[0],
                                    task.taskname.toString(),
                                    task.Date.toString()

                                )
                                newArrayList.add(taskInfo)
                                listId.add(task.idtask.toString())
                                listCateg.add(task.categ.toString())
                                newRecyclerView.adapter = adapters
                                positionList.add(positionIterator)
                                positionIterator++
                            } else {
                                if (task.categ == "Покупки") {
                                    val taskInfo = taskInf(
                                        imageIdArray[3],
                                        task.taskname.toString(),
                                        task.Date.toString()

                                    )
                                    newArrayList.add(taskInfo)
                                    listId.add(task.idtask.toString())
                                    listCateg.add(task.categ.toString())
                                    newRecyclerView.adapter = adapters
                                    positionList.add(positionIterator)
                                    positionIterator++
                                } else {
                                    if (task.categ == "Доставка") {
                                        val taskInfo = taskInf(
                                            imageIdArray[1],
                                            task.taskname.toString(),
                                            task.Date.toString()

                                        )
                                        newArrayList.add(taskInfo)
                                        listId.add(task.idtask.toString())
                                        listCateg.add(task.categ.toString())
                                        newRecyclerView.adapter = adapters
                                        positionList.add(positionIterator)
                                        positionIterator++
                                    } else {
                                        if (task.categ == "Сопровождение") {
                                            val taskInfo = taskInf(
                                                imageIdArray[2],
                                                task.taskname.toString(),
                                                task.Date.toString()

                                            )
                                            newArrayList.add(taskInfo)
                                            listId.add(task.idtask.toString())
                                            listCateg.add(task.categ.toString())
                                            newRecyclerView.adapter = adapters
                                            positionList.add(positionIterator)
                                            positionIterator++
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
            mDatabasResponse.addValueEventListener(vListener)
            progress.visibility = View.GONE
            newRecyclerView.visibility=View.VISIBLE
        }
        getDataFromDB()

    }
    override fun onStart() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeListener,filter)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(networkChangeListener)
        super.onStop()
    }

    override fun onItemClick(position: Int) {
        val clickedItem: taskInf =newArrayList[position]
        val declineOrAcceptIntent = Intent(this, declineoraccept::class.java)
        declineOrAcceptIntent.putExtra("taskid",listId[position])
        declineOrAcceptIntent.putExtra("userid",userId)
        declineOrAcceptIntent.putExtra("position",positionList[position])
        declineOrAcceptIntent.putExtra("heading",clickedItem.nameTask)
        declineOrAcceptIntent.putExtra("heading2",clickedItem.dateTask)
        declineOrAcceptIntent.putExtra("image",clickedItem.titleImage)
        declineOrAcceptIntent.putExtra("type",listCateg[position])
        startActivity(declineOrAcceptIntent)
        overridePendingTransition(1,1)
    }

}