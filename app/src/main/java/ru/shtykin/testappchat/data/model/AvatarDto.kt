package ru.shtykin.testappchat.data.model

import com.google.gson.annotations.SerializedName

data class AvatarDto(
    @SerializedName("filename") val filename: String,
    @SerializedName("base_64") val base64: String,
)