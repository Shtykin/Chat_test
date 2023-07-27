package ru.shtykin.testappchat.domain.usecase

import ru.shtykin.testappchat.domain.Repository

class GetBitmapFromUrlUseCase(private val repository: Repository) {
    suspend fun execute(url: String) =
        repository.getBitmapFromUrl(url)
}