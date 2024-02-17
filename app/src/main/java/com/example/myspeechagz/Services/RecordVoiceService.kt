package com.example.myspeechagz.Services

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.example.myspeechagz.Helpers.AudioRecorder
import com.example.myspeechagz.MainActivity
import com.example.myspeechagz.R


class RecordVoiceService : Service() {

    companion object {
        const val LOG_TAG = "AudioRecordService"
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "YOUR_CHANNEL_ID"
        const val MAX_SILENCE_DURATION = 10000 // 10 seconds
        const val MSG_STOP_RECORDING = 1

    }
    private val mediaRecorder: MediaRecorder? = null
    private val mediaPlayer: MediaPlayer? = null
    private var handler: Handler? = null
    private val isRecording = false
    private var count: Int = 0
    private var audioRecorder: AudioRecorder? = null
    private var recorder: MediaRecorder? = null
//    private var dir_audio_path=(getExternalFilesDir(null)?.getAbsolutePath() ?: "");


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

//        audioRecorder= AudioRecorder(this);
        Toast.makeText(this, "Background is working ", Toast.LENGTH_SHORT).show()
        startForegroundServiceWithNotification(this);

//        audioRecorder!!.startRecording(dir_audio_path +"/audio.3gp");
//        worked();
        return START_STICKY
    }

    // Method to create the notification channel
    private fun createNotificationChannel(context: Context) {
        val notificationManager = NotificationManagerCompat.from(context)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Your Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
    // Method to start the foreground service with notification
    private fun startForegroundServiceWithNotification(context: Context) {
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Service is running in the background")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .build()

        // Create the notification channel
        createNotificationChannel(context)

        // Start the foreground service with the notification
        startForeground(NOTIFICATION_ID, notification)
    }

    private  fun startMediaRecorder(){


//        recorder =  MediaRecorder();
//        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        recorder!!.setOutputFile(dir_audio_path +"/audio.3gp");
//        recorder!!.prepare();
//        recorder!!.start();
    }

    private  fun AudioCapture(){}


    private  fun worked(){

        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what === MSG_STOP_RECORDING) {

                }
            }
        }
        val silenceCheckRunnable: Runnable = object : Runnable {
            override fun run() {

                println(" Runing .... "+count);

                if (count++    >= 30) {
                    // Stop recording if silence duration exceeds the maximum allowed
                    (handler as Handler).sendEmptyMessage(MSG_STOP_RECORDING)
                } else {
                    // Check again after a short delay
                    (handler as Handler).postDelayed(this, 500) // Check every 500 milliseconds
                }


            }
        }
        (handler as Handler).postDelayed(silenceCheckRunnable, 500) // Initial check after 500 milliseconds

    }

    override fun stopService(name: Intent?): Boolean {
        Log.d("Stopping","Stopping Service")

        return super.stopService(name)
    }
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}