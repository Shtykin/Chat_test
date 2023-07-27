package ru.shtykin.testappchat.domain.entity

data class Message(
    val text: String,
    val sender: Sender,
    val timeStamp: Long
)
