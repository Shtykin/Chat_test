package ru.shtykin.weatherapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.shtykin.testappchat.domain.Repository
import ru.shtykin.testappchat.domain.usecase.GetCurrentRegionUseCase

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetCurrentRegionUseCase(repository: Repository): GetCurrentRegionUseCase =
        GetCurrentRegionUseCase(repository)
}