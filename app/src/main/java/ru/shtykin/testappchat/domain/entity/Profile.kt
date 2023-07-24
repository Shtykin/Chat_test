package ru.shtykin.testappchat.domain.entity

data class Profile(
    val name: String,
    val username: String,
    val birthday: String,
    val city: String,
    val avatar: String,
)