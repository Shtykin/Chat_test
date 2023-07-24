package ru.shtykin.testappchat.data.network.model

import com.google.gson.annotations.SerializedName

data class RequestPutProfileDto(
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("city") val city: String,
    @SerializedName("vk") val vk: String,
    @SerializedName("instagram") val instagram: String,
    @SerializedName("status") val status: String,
    @SerializedName("avatar") val avatar: AvatarDto,
)