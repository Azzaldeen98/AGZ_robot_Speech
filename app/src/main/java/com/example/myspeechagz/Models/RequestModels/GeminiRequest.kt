package com.example.myspeechagz.Models.RequestModels

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(
    val _content:String
)
