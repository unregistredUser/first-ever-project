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

class auth_or_reg : AppCompatActivity() {
    val networkChangeListener =
        NetworkChangeListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_or_reg)

        //init

        val emailInput = findViewById<EditText>(R.id.email)
        val userInfoIntent = Intent(this, userinfo::class.java)
        val passwordInput = findViewById<EditText>(R.id.password)
        val toPreviousActButt = findViewById<TextView>(R.id.exit)
        val toAuthActButt = findViewById<TextView>(R.id.log)
        val arguments = intent.extras
        val typeUser = arguments!!["type"].toString()
        val toUserInfoActButt = findViewById<Button>(R.id.mains)

        //init

        toPreviousActButt.setOnClickListener {
            val chooseRolesIntent = Intent(this, chooseRoles::class.java)
            startActivity(chooseRolesIntent)
            overridePendingTransition(1, 1)
        }
        toAuthActButt.setOnClickListener {
            val authIntent = Intent(this, Auth::class.java)
            authIntent.putExtra("typed", typeUser)
            startActivity(authIntent)
            overridePendingTransition(1, 1)
        }

        toUserInfoActButt.setOnClickListener {
            val passwordInputText = passwordInput.getText().toString()
            val emailInputText = emailInput.getText().toString()

            if (emailInputText.isEmpty() && passwordInputText.isEmpty()) {
                "неккоректный email".also { emailInput.hint = it }
                "неккоректный пароль".also { passwordInput.hint = it }
            } else {
                if (emailInputText.isEmpty() || emailInputText == " ") {
                    "неккоректный email".also { emailInput.hint = it }
                    emailInput.text = null
                } else {
                    if (passwordInputText.isEmpty() || passwordInputText == " ") {
                        "неккоректный пароль".also { passwordInput.hint = it }
                        passwordInput.text = null
                    } else {
                        val Firebase = FirebaseAuth.getInstance()
                        Firebase.createUserWithEmailAndPassword(
                            emailInput.getText().toString(),
                            passwordInput.getText().toString()
                        )
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    Log.d(ContentValues.TAG, "Регистрация прошла успешно")
                                    val user = Firebase.currentUser
                                    userInfoIntent.putExtra("id", user?.uid)
                                    userInfoIntent.putExtra("type", typeUser)
                                    startActivity(userInfoIntent)
                                    overridePendingTransition(1, 1)

                                    //updateUI(user)
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(
                                        ContentValues.TAG,
                                        "createUserWithEmail:failure",
                                        task.exception
                                    )
                                    Toast.makeText(
                                        baseContext, "Ошибка регистрации",
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
        val uriCopyText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
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