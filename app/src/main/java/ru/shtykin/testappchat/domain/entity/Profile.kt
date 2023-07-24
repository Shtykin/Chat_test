package ru.shtykin.testappchat.domain.entity

import android.graphics.Bitmap

data class Profile(
    val phone: String,
    val name: String,
    val username: String,
    val birthday: String,
    val zodiacSign: String,
    val city: String,
    val about: String,
    val avatar: Bitmap?,
)