package ru.shtykin.testappchat.domain.usecase

import ru.shtykin.testappchat.domain.Repository

class GetCurrentRegionUseCase(private val repository: Repository) {
    fun execute() = repository.getCurrentRegion()
}