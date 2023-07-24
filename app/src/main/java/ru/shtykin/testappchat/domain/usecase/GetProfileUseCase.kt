package ru.shtykin.testappchat.domain.usecase

import ru.shtykin.testappchat.domain.Repository

class GetProfileUseCase(private val repository: Repository) {
    suspend fun execute() =  repository.getProfile()
}