package ru.shtykin.testappchat.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.shtykin.testappchat.domain.Repository
import ru.shtykin.testappchat.domain.usecase.CheckAuthCodeUseCase
import ru.shtykin.testappchat.domain.usecase.RegistrationUseCase
import ru.shtykin.testappchat.domain.usecase.SendAuthCodeUseCase

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideRegistrationUseCase(repository: Repository): RegistrationUseCase =
        RegistrationUseCase(repository)

    @Provides
    fun provideSendAuthCodeUseCase(repository: Repository): SendAuthCodeUseCase =
        SendAuthCodeUseCase(repository)
    @Provides
    fun provideCheckAuthCodeUseCaseUseCase(repository: Repository): CheckAuthCodeUseCase =
        CheckAuthCodeUseCase(repository)
}