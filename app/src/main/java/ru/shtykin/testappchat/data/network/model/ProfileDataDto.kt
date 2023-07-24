package ru.shtykin.testappchat.data.network.model

import com.google.gson.annotations.SerializedName

data class ProfileDataDto(
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("city") val city: String,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("avatar") val avatar: String,
)