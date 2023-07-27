package com.example.newdiplom.domain.presentation

import android.content.ContentValues
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.example.newdiplom.R
import com.example.newdiplom.domain.entities.fireBaseClasses.CustomNewUser
import com.example.newdiplom.domain.entities.fireBaseClasses.NewUser
import com.example.newdiplom.domain.entities.fireBaseClasses.NewVolunteer
import com.example.newdiplom.domain.entities.networkChangeListener.NetworkChangeListener
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Matcher
import java.util.regex.Pattern

class userinfo : AppCompatActivity() {
    val networkChangeListener =
        NetworkChangeListener()
    var emptyInputsNum=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init

        setContentView(R.layout.activity_userinfo)
        val arguments = intent.extras
        val idUser = arguments!!["id"].toString()
        val typeUser = arguments!!["type"].toString()
        var userGender = ""
        val newVolunteerKey = "Volunteer"
        val userKey = "User"
        var userFirebaseRef = FirebaseDatabase.getInstance().getReference(userKey)
        val buttContainer = findViewById<RadioGroup>(R.id.buttonview)

        //init

        if (typeUser == "v") {
            userFirebaseRef = FirebaseDatabase.getInstance().getReference(newVolunteerKey)
        }


        setContentView(R.layout.activity_userinfo)
        val authOrRegIntent = Intent(this, auth_or_reg::class.java)
        val profileVoluntIntent = Intent(this, profilevolunteer::class.java)
        val profileCustomerIntent = Intent(this, profile::class.java)
        val buttToPreviousACt = findViewById<TextView>(R.id.exit4)
        val ageUserView = findViewById<TextView>(R.id.age)
        val buttApproveReg = findViewById<Button>(R.id.approved)
        val editName = findViewById<EditText>(R.id.editname)
        val editFamil = findViewById<EditText>(R.id.editfamil)
        val editPhone = findViewById<EditText>(R.id.editphone)

        buttToPreviousACt.setOnClickListener {

            startActivity(authOrRegIntent)
            overridePendingTransition(1, 1)
        }

        buttApproveReg.setOnClickListener {
            val editNameText = editName.getText().toString()
            val editFamilText = editFamil.getText().toString()
            val editPhoneText = editPhone.getText().toString()
            val editAgeText = ageUserView.getText().toString()
            if (typeUser == "z") {
                val radiobuttChecked = buttContainer.checkedRadioButtonId
                if (radiobuttChecked == R.id.female) {
                    userGender = "Женщина"
                } else {
                    if (radiobuttChecked == R.id.male) {
                        userGender = "Мужчина"

                    }
                }



                val newUser =
                    NewUser(
                        idUser,
                        editNameText,
                        editFamilText,
                        editPhoneText,
                        editAgeText,
                        5.0,
                        0,
                        userGender
                    )
                if (checkPhonePattern(editPhoneText)) {
                    if (editNameText.isEmpty()) {
                        "неккоректное имя".also { editName.hint = it }
                    } else {
                        if (editAgeText.isEmpty()) {
                            "неккоректный возраст".also { ageUserView.hint = it }
                        } else {
                            userFirebaseRef.push().setValue(newUser)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(ContentValues.TAG, "createUserInfo:success")
                                        profileCustomerIntent.putExtra("uid", idUser)
                                        startActivity(profileCustomerIntent)

                                    } else {

                                        Log.w(
                                            ContentValues.TAG,
                                            "createUserInfo:failure",
                                            task.exception
                                        )
                                        Toast.makeText(
                                            baseContext, "Ошибка регистрации",
                                            Toast.LENGTH_SHORT
                                        ).show()


                                    }
                                }
                        }

                    }

                } else {
                    "неккоректный номер телефона".also { editPhone.hint = it }
                    editPhone.text = null
                }

            } else {
                    val idchecked = buttContainer.checkedRadioButtonId
                    if (idchecked == R.id.female) {
                        userGender = "Женщина"
                    } else {
                        if (idchecked == R.id.male) {
                            userGender = "Мужчина"

                        }
                    }

                    val newVolunteer =
                        NewVolunteer(
                            idUser,
                            editNameText,
                            editFamilText,
                            editPhoneText,
                            editAgeText,
                            5.0,
                            0,
                            userGender
                        )
                    if (checkPhonePattern(editPhoneText)) {
                        if (editNameText.isEmpty()) {
                            "неккоректный email".also { editName.hint = it }
                        } else {
                            if (editAgeText.isEmpty()) {
                                "неккоректный email".also { ageUserView.hint = it }
                            } else {
                                userFirebaseRef.push().setValue(newVolunteer)
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful) {

                                            Log.d(ContentValues.TAG, "createVolunteerInfo:success")
                                            profileVoluntIntent.putExtra("uid", idUser)
                                            startActivity(profileVoluntIntent)
                                            overridePendingTransition(1, 1)

                                        } else {

                                            Log.w(
                                                ContentValues.TAG,
                                                "createVolunteerInfo:failure",
                                                task.exception
                                            )
                                            Toast.makeText(
                                                baseContext, "Ошибка регистрации",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }
                                    }
                            }
                        }
                    } else {
                        "пустое поле".also { editPhone.hint = it }
                        editPhone.text = null
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

    fun checkInputText(textInput: String)
    {
        if(textInput.isEmpty())
        {
            emptyInputsNum+=1
        }
    }

    fun checkPhonePattern(phoneNum:String) : Boolean
    {
        val pattern: Pattern =
            Pattern.compile("^\\s*\\+?375((33\\d{7})|(29\\d{7})|(44\\d{7}|)|(25\\d{7}))\\s*\$")
        val matcher: Matcher = pattern.matcher(phoneNum)

        return matcher.find()
    }


}