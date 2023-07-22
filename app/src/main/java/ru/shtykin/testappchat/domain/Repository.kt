package ru.shtykin.testappchat.domain

interface Repository {
    fun getCurrentRegion(): String
}