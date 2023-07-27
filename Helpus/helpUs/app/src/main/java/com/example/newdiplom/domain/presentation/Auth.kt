package com.example.newdiplom.domain.presentation

import android.content.ContentValues
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.newdiplom.R
import com.example.newdiplom.domain.entities.networkChangeListener.NetworkChangeListener
import com.google.firebase.auth.FirebaseAuth

class Auth : AppCompatActivity() {
    val networkChangeListener =
        NetworkChangeListener()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val authIntent = Intent(this, auth_or_reg::class.java)
        val emailInput = findViewById<EditText>(R.id.editmail)
        val passwordInput = findViewById<EditText>(R.id.editpass)
        val buttToPreviosAct = findViewById<TextView>(R.id.exit3)
        val databaseRefForUSerAuth = FirebaseAuth.getInstance()
        val logClick = findViewById<Button>(R.id.blog)
        val arguments = intent.extras
        val typeUser = arguments!!["typed"].toString()
        val ifCorrectRole = arguments!!["CorrectRoleOrNot"].toString()

        buttToPreviosAct.setOnClickListener {
            authIntent.putExtra("type", typeUser)
            startActivity(authIntent)
            overridePendingTransition(1,1)
        }
        if (ifCorrectRole == "no") {
            Toast.makeText(this, "Вы выбрали не ту роль", Toast.LENGTH_SHORT).show()
        }
        logClick.setOnClickListener {

            if (emailInput.getText().isEmpty() && passwordInput.getText().isEmpty()) {
                "неккоректный пароль".also { emailInput.hint = it }
                "неккоректный email".also { passwordInput.hint = it }
            } else {
                if (emailInput.getText().isEmpty()) {
                    emailInput.hint = "неккоректный email"
                } else {
                    if (passwordInput.getText().isEmpty()) {
                        "неккоректный пароль".also { passwordInput.hint = it }
                    } else {
                        databaseRefForUSerAuth.signInWithEmailAndPassword(
                            emailInput.getText().toString(),
                            passwordInput.getText().toString()
                        )
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    val userAuth = databaseRefForUSerAuth.currentUser
                                    if (userAuth != null) {

                                        Log.d(ContentValues.TAG, "Успешная авторизация")
                                        val user = databaseRefForUSerAuth.currentUser
                                        if (typeUser == "z") {
                                            val profileIntent = Intent(this, profile::class.java)
                                            profileIntent.putExtra("uid", user?.uid)
                                            profileIntent.putExtra("taskorprof", "prof")
                                            startActivity(profileIntent)
                                            overridePendingTransition(1,1)

                                        } else {

                                            val profileVoluntIntent = Intent(this, profilevolunteer::class.java)
                                            profileVoluntIntent.putExtra("uid", user?.uid)
                                            profileVoluntIntent.putExtra("taskorprof", "prof")
                                            startActivity(profileVoluntIntent)
                                            overridePendingTransition(1,1)
                                        }
                                    } else {
                                        Toast.makeText(
                                            baseContext, "Такого пользователя не существует",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    //updateUI(user)
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(
                                        ContentValues.TAG,
                                        "createUserWithEmail:failure",
                                        task.exception
                                    )
                                    Toast.makeText(
                                        baseContext, "Ошибка авторизации",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    //updateUI(null)
                                }
                            }


                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uriCopyText= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM,Uri::class.java)
        } else {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM)
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