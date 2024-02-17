package com.example.myspeechagz

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myspeechagz.ApiClient.Controlls.GeminiControll
import com.example.myspeechagz.ContentApp.ContentApp
import com.example.myspeechagz.Services.RecordVoiceService
import com.google.ai.client.generativeai.Chat
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private  var chat: Chat? = null
    private  var geminiControll: GeminiControll? = null
    private var btnSend: TextView ? = null;
    private var textResult: TextView ? = null;
    private var textInput: TextInputEditText? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSend=findViewById<TextView>(R.id.btnSend);
        textResult=findViewById<TextView>(R.id.textResult);
        textInput=findViewById<TextInputEditText>(R.id.textInput);

        checkMicrophonPermision();
        geminiControll= GeminiControll(this);
        chat=geminiControll!!.getChat();

    }
    fun startRecordServicesOnForground(){

        if(!isRecordServiceRunningInForeground(this, RecordVoiceService::class.java)) {
            val  serviceIntent = Intent(this, RecordVoiceService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                startForegroundService(serviceIntent)
            else
                startService(serviceIntent)
        }
    }
    fun checkMicrophonPermision(){


        // Check if the permission has been granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it from the user
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                ContentApp.REQUEST_MICROPHONE_PERMISSION_CODE)
        } else {
            // Permission has already been granted, proceed with your app logic

        }

        // Register the permissions callback, which handles the user's response to the
        // system permissions dialog. Save the return value, an instance of
        // ActivityResultLauncher. You can use either a val, as shown in this snippet,
        // or a lateinit var in your onAttach() or onCreate() method.
//        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts
//            .RequestPermission()) { isGranted: Boolean ->
//                if (isGranted) {
//                    // Permission is granted. Continue the action or workflow in your
//                    // app.
//                } else {
//                    // Explain to the user that the feature is unavailable because the
//                    // feature requires a permission that the user has denied. At the
//                    // same time, respect the user's decision. Don't link to system
//                    // settings in an effort to convince the user to change their
//                    // decision.
//
//                }
//            }
//
//        when {
//            ContextCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.RECORD_AUDIO
//            ) == PackageManager.PERMISSION_GRANTED -> {
//                // You can use the API that requires the permission.
////                performAction(...)
//            }
//            ActivityCompat.shouldShowRequestPermissionRationale(
//                this, android.Manifest.permission.RECORD_AUDIO) -> {
//                // In an educational UI, explain to the user why your app requires this
//                // permission for a specific feature to behave as expected, and what
//                // features are disabled if it's declined. In this UI, include a
//                // "cancel" or "no thanks" button that lets the user continue
//                // using your app without granting the permission.
////                showInContextUI(...)
//            }
//            else -> {
//                // You can directly ask for the permission.
//                requestPermissions(arrayOf(android.Manifest.permission.RECORD_AUDIO),
//                    ContentApp.REQUEST_MICROPHONE_PERMISSION_CODE)
//            }
//        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ContentApp.REQUEST_MICROPHONE_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, proceed with your app logic
                    startRecordServicesOnForground();
                } else {
                    // Permission denied, handle accordingly (e.g., show explanation, disable functionality, etc.)
                }
                return
            }
            // Add more cases for other permissions if needed
        }
    }
    fun isRecordServiceRunningInForeground(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }
    fun sendMessage(view: View) {

        if(!textInput?.text.isNullOrEmpty()) {


            runOnUiThread { textResult?.setText("");}
            GlobalScope.launch {
                val response = chat!!.sendMessage(textInput?.text.toString())
                if(response!=null) {
                    runOnUiThread {

                        textResult?.visibility = View.VISIBLE;
                         textResult?.setText(response.text);
                         textInput?.setText("");
                    }
                }
            }
        }
    }





}