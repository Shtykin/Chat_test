package ru.shtykin.testappchat.domain.usecase

import ru.shtykin.testappchat.domain.Repository

class RegistrationUseCase(private val repository: Repository) {
    suspend fun execute(phone: String, name: String, userName: String) =
        repository.registration(phone, name, userName)
}