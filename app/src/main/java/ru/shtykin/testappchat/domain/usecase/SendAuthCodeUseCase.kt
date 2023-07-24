package ru.shtykin.testappchat.domain.usecase

import ru.shtykin.testappchat.domain.Repository

class SendAuthCodeUseCase(private val repository: Repository) {
    suspend fun execute(phone: String,) =
        repository.sendAuthCode(phone)
}