package ru.shtykin.testappchat.data.model

import com.google.gson.annotations.SerializedName

data class AvatarsDto(
    @SerializedName("avatar") val avatar: String,
    @SerializedName("bigAvatar") val bigAvatar: String,
    @SerializedName("miniAvatar") val miniAvatar: String,
)