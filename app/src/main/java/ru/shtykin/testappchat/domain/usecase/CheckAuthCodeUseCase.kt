package ru.shtykin.testappchat.domain.usecase

import ru.shtykin.testappchat.domain.Repository

class CheckAuthCodeUseCase(private val repository: Repository) {
    suspend fun execute(phone: String, code: String) =
        repository.checkAuthCode(phone, code)
}