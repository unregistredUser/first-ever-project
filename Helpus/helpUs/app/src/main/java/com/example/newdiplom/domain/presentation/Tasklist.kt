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
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newdiplom.domain.entities.customRecyclerAdapter.MyAdapter
import com.example.newdiplom.R
import com.example.newdiplom.domain.entities.fireBaseClasses.NewTask
import com.example.newdiplom.domain.entities.networkChangeListener.NetworkChangeListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Tasklist : AppCompatActivity(), MyAdapter.OnItemClickListener {
    val networkChangeListener =
        NetworkChangeListener()
    //preinit

    private val taskKey = "Tasks"
    private val responseKey = "Responces"
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<taskInf>
    lateinit var imageId: Array<Int>
    var typeTask = ""
    var listId = ArrayList<String>()
    var userId = ""
    var posList = ArrayList<Int>()

    //preinitend
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasklist)

        //init

        val taskDatabaseRef = FirebaseDatabase.getInstance().getReference(taskKey)
        imageId = arrayOf(
            R.drawable.housework,
            R.drawable.deliverytask,
            R.drawable.helpgotask,
            R.drawable.shopify
        )
        newRecyclerView = findViewById(R.id.recyclertask)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        newRecyclerView.visibility = View.GONE
        
        val adapters = MyAdapter(newArrayList, this)
        val progress = findViewById<ProgressBar>(R.id.progressBar2)
        val buttToProfileAct = findViewById<ImageView>(R.id.profiled)
        val buttToCallACt = findViewById<ImageView>(R.id.searchtask)
        val taskCategoryView = findViewById<TextView>(R.id.urtask3)
        var i = 0
        val arguments = intent.extras
        val typeTaskArg = arguments!!["typeTask"].toString()
        userId = arguments!!["userId"].toString()
        
        progress.visibility = View.VISIBLE
        taskCategoryView.text = typeTaskArg
        typeTask = typeTaskArg
        

        //init

        buttToProfileAct.setOnClickListener {
            val profileVoluntIntent = Intent(this, profilevolunteer::class.java)
            profileVoluntIntent.putExtra("uid", userId)
            startActivity(profileVoluntIntent)
            overridePendingTransition(1,1)
        }

        buttToCallACt.setOnClickListener {
            val taskIntent = Intent(this, Tasks::class.java)
            taskIntent.putExtra("idus", userId)
            startActivity(taskIntent)
            overridePendingTransition(1,1)
        }

        fun getTaskInfFromDb() {

            val vListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {


                    for (ds in dataSnapshot.children) {

                        val task: NewTask = ds.getValue(
                            NewTask::class.java)!!
                        if (task.difficulty == typeTaskArg) {
                            if (typeTaskArg == "Работа по дому") {
                                val taskInf = taskInf(
                                    imageId[0],
                                    task.taskname.toString(),
                                    "c " + task.date1 + " по " + task.date2
                                )

                                newArrayList.add(taskInf)
                                listId.add(task.idtask.toString())
                                newRecyclerView.adapter = adapters
                                posList.add(i)
                                i++

                            } else {
                                if (typeTaskArg == "Покупки") {
                                    val taskInf = taskInf(
                                        imageId[3],
                                        task.taskname.toString(),
                                        "c " + task.date1 + " по " + task.date2
                                    )

                                    newArrayList.add(taskInf)
                                    listId.add(task.idtask.toString())
                                    newRecyclerView.adapter = adapters
                                    posList.add(i)
                                    i++

                                } else {
                                    if (typeTaskArg == "Доставка") {
                                        val taskInf = taskInf(
                                            imageId[1],
                                            task.taskname.toString(),
                                            "c " + task.date1 + " по " + task.date2

                                        )

                                        newArrayList.add(taskInf)
                                        listId.add(task.idtask.toString())
                                        newRecyclerView.adapter = adapters
                                        posList.add(i)
                                        i++

                                    } else {
                                        if (typeTaskArg == "Сопровождение") {
                                            val taskInf = taskInf(
                                                imageId[2],
                                                task.taskname.toString(),
                                                "c " + task.date1 + " по " + task.date2
                                            )

                                            newArrayList.add(taskInf)
                                            listId.add(task.idtask.toString())
                                            newRecyclerView.adapter = adapters
                                            posList.add(i)
                                            i++

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
            taskDatabaseRef.addValueEventListener(vListener)
            progress.visibility = View.GONE
            newRecyclerView.visibility = View.VISIBLE
        }
        getTaskInfFromDb()


    }

    override fun onItemClick(position: Int) {
        val clickedItem: taskInf = newArrayList[position]
        val acceptTaskIntent = Intent(this, AcceptTask::class.java)
        acceptTaskIntent.putExtra("taskid", listId[position])
        acceptTaskIntent.putExtra("iduser", userId)
        acceptTaskIntent.putExtra("heading1", clickedItem.nameTask)
        acceptTaskIntent.putExtra("heading2", clickedItem.dateTask)
        acceptTaskIntent.putExtra("image", clickedItem.titleImage)
        acceptTaskIntent.putExtra("typeTask", typeTask)
        startActivity(acceptTaskIntent)
        overridePendingTransition(1,1)
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

