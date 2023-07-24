package ru.shtykin.testappchat.domain.entity

data class UserTokens(
    val accessToken: String,
    val refreshToken: String,
    val userId: Long
)