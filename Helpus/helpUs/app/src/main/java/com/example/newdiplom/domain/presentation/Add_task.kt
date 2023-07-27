package com.example.newdiplom.domain.presentation

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.newdiplom.R
import com.example.newdiplom.domain.entities.fireBaseClasses.NewTask
import com.example.newdiplom.domain.entities.networkChangeListener.NetworkChangeListener
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class add_task : AppCompatActivity() {

    private lateinit var btnDatePicker: EditText
    val networkChangeListener =
        NetworkChangeListener()
    val myFormat = "dd-MM"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        //init

        val TASK = "Tasks"
        val mDatabas = FirebaseDatabase.getInstance().getReference(TASK)
        val diff = findViewById<Spinner>(R.id.difficulty)
        val nametask = findViewById<EditText>(R.id.tasknameedit)
        val addtaskbt = findViewById<Button>(R.id.addtaskbutt)
        val chatbtn = findViewById<ImageView>(R.id.call)
        val mytsk = findViewById<ImageView>(R.id.mytasks)
        val torpof = findViewById<ImageView>(R.id.toprofile)
        val args = intent.extras
        val iduser = args!!["userid"].toString()



        btnDatePicker = findViewById(R.id.btnDatePicker)
        chatbtn.setOnClickListener {
            val Tasks = Intent(this, responses::class.java)
            Tasks.putExtra("uid", iduser)
            startActivity(Tasks)
            overridePendingTransition(1, 1)
        }


        torpof.setOnClickListener {
            val Tasks = Intent(this, profile::class.java)
            Tasks.putExtra("uid", iduser)
            startActivity(Tasks)
            overridePendingTransition(1, 1)
        }

        mytsk.setOnClickListener {
            val Tasks = Intent(this, mytasks::class.java)
            Tasks.putExtra("uid", iduser)
            startActivity(Tasks)
            overridePendingTransition(1, 1)
        }
        val myCalendar = Calendar.getInstance()

        val DatePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar)
        }

        //init

        btnDatePicker.setOnClickListener {
            DatePickerDialog(
                this,
                DatePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        addtaskbt.setOnClickListener {
            val iduser = args!!["userid"].toString()
            val idtaask = iduser
            val namet = nametask.getText().toString()
            val dif = diff.selectedItem.toString()
            val formatter = SimpleDateFormat("dd-MM")
            val date = formatter.format(Date()).toString()
            val date2 = btnDatePicker.getText().toString()
            val newtask =
                NewTask(
                    idtaask,
                    iduser,
                    namet,
                    dif,
                    date,
                    date2
                )
            val name = nametask.getText().toString()

            if (name.isEmpty()) {

                "неккоректный название задачи".also { nametask.hint = it }

            } else {
                FirebaseDatabase.getInstance().getReference().child("/Tasks/" + iduser)
                    .setValue(newtask)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "createTask:success")
                            Toast.makeText(
                                baseContext,
                                "Задача успешно добавлена",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "createTask:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Ошибка создания задачи.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }


        }
    }

    private fun updateLable(myCalendar: Calendar) {

        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        btnDatePicker.setText(sdf.format(myCalendar.time))
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