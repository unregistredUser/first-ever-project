package com.example.newdiplom.domain.entities.networkChangeListener

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatButton
import com.example.newdiplom.R
import com.example.newdiplom.domain.entities.connectivityChecker.isInternetConnected

class NetworkChangeListener : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (!isInternetConnected.isConnectedToInternet(context)) {
            val builder = AlertDialog.Builder(context)
            val layout_dialog =
                LayoutInflater.from(context).inflate(R.layout.activity_check_internetdialog, null)
            builder.setView(layout_dialog)
            val btnRetry = layout_dialog.findViewById<AppCompatButton>(R.id.btnRetry)
            val dialog = builder.create()
            dialog.show()
            dialog.setCancelable(false)
            dialog.window!!.setGravity(Gravity.CENTER)
            btnRetry.setOnClickListener {
                dialog.dismiss()
                onReceive(context, intent)
            }
        }
    }
}