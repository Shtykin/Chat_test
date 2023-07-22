package ru.shtykin.testappchat.domain.entity

data class Country(
    val region: String,
    val country: String,
    val code: String,
    val flagEmoji: String?
)