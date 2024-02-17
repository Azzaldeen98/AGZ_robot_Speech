package com.example.myspeechagz.Helpers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioRecorder {
    private static final int RECORDER_SAMPLE_RATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

    private AudioRecord audioRecorder;
    private AudioTrack audioTrack;
    private Thread recordingThread;
    private boolean isRecording;
    private Context context;

    public  AudioRecorder(Context context){
        this.context = context;
    }

    public void startRecording(String filePath) {

        if(context==null){
            Log.d("Erro","context is null refrence");
            return;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        audioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDER_SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, BUFFER_SIZE);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, RECORDER_SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, RECORDER_AUDIO_ENCODING, BUFFER_SIZE, AudioTrack.MODE_STREAM);

        audioRecorder.startRecording();
        audioTrack.play();

        isRecording = true;

        recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[BUFFER_SIZE];
                FileOutputStream outputStream = null;

                try {
                    outputStream = new FileOutputStream(filePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                while (isRecording) {
                    int read = audioRecorder.read(buffer, 0, BUFFER_SIZE);
                    audioTrack.write(buffer, 0, read);

                    try {
                        outputStream.write(buffer, 0, read);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        recordingThread.start();
    }

    public void stopRecording() {
        isRecording = false;

        try {
            recordingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        audioRecorder.stop();
        audioRecorder.release();
        audioTrack.stop();
        audioTrack.release();
    }
}