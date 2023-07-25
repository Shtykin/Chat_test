package ru.shtykin.testappchat.domain.usecase

import ru.shtykin.testappchat.domain.Repository
import ru.shtykin.testappchat.domain.entity.Profile

class PutProfileUseCase(private val repository: Repository) {
    suspend fun execute(profile: Profile) =  repository.putProfile(profile)
}