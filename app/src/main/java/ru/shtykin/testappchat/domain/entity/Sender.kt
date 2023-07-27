package ru.shtykin.testappchat.domain.entity

sealed class Sender{
    object Me: Sender()
    data class NotMe(
        val guest: Guest
    ): Sender()
}
