package ru.shtykin.testappchat.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.shtykin.testappchat.domain.Repository
import ru.shtykin.testappchat.domain.usecase.RegistrationUseCase

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideRegistrationUseCase(repository: Repository): RegistrationUseCase =
        RegistrationUseCase(repository)
}