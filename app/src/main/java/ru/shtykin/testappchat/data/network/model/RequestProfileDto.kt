package ru.shtykin.testappchat.data.network.model

import com.google.gson.annotations.SerializedName

data class RequestProfileDto(
    @SerializedName("profile_data") val profileDataDto: ProfileDataDto,
)