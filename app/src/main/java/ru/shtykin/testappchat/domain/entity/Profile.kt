package ru.shtykin.testappchat.domain.entity

import android.graphics.Bitmap

data class Profile(
    val name: String,
    val username: String,
    val birthday: String,
    val city: String,
    val avatar: Bitmap?,
)