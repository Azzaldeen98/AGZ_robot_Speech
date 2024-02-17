package com.example.myspeechagz.ApiClient.Controlls

import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getString
import com.example.myspeechagz.MainActivity
import com.example.myspeechagz.R
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.*

public  class GeminiControll(mainActivity: MainActivity) {

    private var context:Context?=null;
    private  var chat: Chat? = null;


    fun getChat(): Chat? { return chat }
    fun GeminiControll(context:Context) {
        this.context=context;
        generatModel();
    }
    private fun generatModel() {


        val model = GenerativeModel(
            "gemini-pro",
            // Retrieve API key as an environmental variable defined in a Build Configuration
            // see https://github.com/google/secrets-gradle-plugin for further instructions
            getString(context!!,R.string.gemini_api_key),
            generationConfig = generationConfig {
                temperature = 0.9f
                topK = 1
                topP = 1f
                maxOutputTokens = 2048
            },
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE),
            ),
        )

        val chatHistory = listOf<Content>()
        chat = model.startChat(chatHistory)
    }
}