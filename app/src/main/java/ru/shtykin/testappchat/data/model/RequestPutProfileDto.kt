package ru.shtykin.testappchat.data.model

import com.google.gson.annotations.SerializedName

data class RequestPutProfileDto(
    @SerializedName("avatars") val avatars: AvatarsDto,
)