package com.example.newdiplom.domain.presentation

import android.content.*
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.newdiplom.R
import com.example.newdiplom.domain.entities.fireBaseClasses.NewResponse
import com.example.newdiplom.domain.entities.fireBaseClasses.NewUser
import com.example.newdiplom.domain.entities.networkChangeListener.NetworkChangeListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class profileforcallzak : AppCompatActivity() {

    val networkChangeListener =
        NetworkChangeListener()

    private val Reskey = "Responces"
    private val Vkey = "User"
    var mobile = "tel:80333429234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profileforcallzak)
        val arguments = intent.extras
        val iduser = arguments!!["iduser"].toString()
        val head1 = arguments!!["heading1"].toString()
        val head2 = arguments!!["heading2"].toString()
        val progress = findViewById<ProgressBar>(R.id.progressBar5)
        val typeofv = findViewById<TextView>(R.id.typeofvolunteer3)
        val num = findViewById<TextView>(R.id.voluntacceptedname)
        val ageinf = findViewById<TextView>(R.id.aboutvoluntdata6)
        val ageuser = findViewById<TextView>(R.id.aboutvoluntage)
        val addtsk = findViewById<ImageView>(R.id.myaccepted)
        val chatbtn = findViewById<ImageView>(R.id.searchtask)
        val torpof = findViewById<ImageView>(R.id.profiled)
        val avtr = findViewById<ImageView>(R.id.avatarsvoluntaccepteed)
        val btn = findViewById<Button>(R.id.callbyphone)
        var idvol = ""

        val mDatabasResponse = FirebaseDatabase.getInstance().getReference(Reskey)
        val userbd = FirebaseDatabase.getInstance().getReference(Vkey)
        //init

        progress.visibility = View.VISIBLE
        num.visibility = View.GONE
        typeofv.visibility = View.GONE
        ageuser.visibility = View.GONE
        ageinf.visibility = View.GONE
        avtr.visibility = View.GONE
        btn.visibility = View.GONE

        chatbtn.setOnClickListener {
            val Tasks = Intent(this, Tasks::class.java)
            Tasks.putExtra("idus", iduser)
            startActivity(Tasks)
            overridePendingTransition(1, 1)
        }
        torpof.setOnClickListener {
            val Tasks = Intent(this, profilevolunteer::class.java)
            Tasks.putExtra("uid", iduser)
            startActivity(Tasks)
            overridePendingTransition(1, 1)
        }

        fun getid() {

            val vListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {


                    for (ds in dataSnapshot.children) {

                        val newResponse: NewResponse = ds.getValue(
                            NewResponse::class.java)!!
                        if (newResponse.taskname == head1 && newResponse.Date == head2) {
                            idvol = newResponse.idtask.toString()

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
        getid()

        fun getUserData() {

            val vListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {


                    for (ds in dataSnapshot.children) {

                        val users: NewUser = ds.getValue(
                            NewUser::class.java)!!
                        if (users.id == idvol) {
                            val names = (users.name)
                            val allname = (users.secname) + " " + names
                            num.text = allname
                            ageinf.text = users.age.toString()
                            mobile = "tel:" + users.phone
                            if (users.gender == "Мужчина") {
                                avtr.setImageResource(R.drawable.profileman)
                            } else {
                                avtr.setImageResource(R.drawable.profilewoman)
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(ContentValues.TAG, "Databasereading:error", databaseError.toException())
                }
            }
            userbd.addValueEventListener(vListener)
            progress.visibility = View.GONE
            num.visibility = View.VISIBLE
            typeofv.visibility = View.VISIBLE
            ageuser.visibility = View.VISIBLE
            ageinf.visibility = View.VISIBLE
            avtr.visibility = View.VISIBLE
            btn.visibility = View.VISIBLE
        }
        getUserData()

        btn.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse(mobile)
            startActivity(callIntent)

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
}

