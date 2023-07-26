package ru.shtykin.testappchat.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.shtykin.testappchat.domain.Repository
import ru.shtykin.testappchat.domain.usecase.CheckAuthCodeUseCase
import ru.shtykin.testappchat.domain.usecase.GetBitmapFromUrlUseCase
import ru.shtykin.testappchat.domain.usecase.GetProfileUseCase
import ru.shtykin.testappchat.domain.usecase.PutProfileUseCase
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
    fun provideCheckAuthCodeUseCase(repository: Repository): CheckAuthCodeUseCase =
        CheckAuthCodeUseCase(repository)

    @Provides
    fun provideGetProfileUseCase(repository: Repository): GetProfileUseCase =
        GetProfileUseCase(repository)

    @Provides
    fun providePutProfileUseCase(repository: Repository): PutProfileUseCase =
        PutProfileUseCase(repository)

    @Provides
    fun provideGetBitmapFromUrlUseCase(repository: Repository): GetBitmapFromUrlUseCase =
        GetBitmapFromUrlUseCase(repository)
}