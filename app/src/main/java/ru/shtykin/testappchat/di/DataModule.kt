package ru.shtykin.weatherapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.shtykin.testappchat.data.repository.RepositoryImpl
import ru.shtykin.testappchat.domain.Repository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRepository(): Repository {
        return RepositoryImpl()
    }

}