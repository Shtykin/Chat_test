package ru.shtykin.weatherapp.di

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.shtykin.testappchat.data.mapper.Mapper
import ru.shtykin.testappchat.data.network.ApiService
import ru.shtykin.testappchat.data.network.AuthInterceptor
import ru.shtykin.testappchat.data.repository.RepositoryImpl
import ru.shtykin.testappchat.domain.Repository
import ru.shtykin.testappchat.settings.AuthStore
import ru.shtykin.testappchat.settings.AuthStoreImpl
import ru.shtykin.testappchat.settings.ProfileStore
import ru.shtykin.testappchat.settings.ProfileStoreImpl
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRepository(
        @Named("UnAuthApiService") unAuthApiService: ApiService,
        @Named("AuthApiService") authApiService: ApiService,
        mapper: Mapper
    ): Repository {
        return RepositoryImpl(unAuthApiService, authApiService, mapper)
    }

    @Provides
    @Singleton
    @Named("UnAuthApiService")
    fun provideApiService(@Named("UnAuthRetrofit") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("AuthApiService")
    fun provideAuthApiService(@Named("AuthRetrofit") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("UnAuthRetrofit")
    fun provideRetrofit(gson: Gson, @Named("UnAuthClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://plannerok.ru/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named("AuthRetrofit")
    fun provideAuthRetrofit(gson: Gson, @Named("AuthClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://plannerok.ru/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named("UnAuthClient")
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    @Named("AuthClient")
    fun provideAuthOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        authStore: AuthStore,
        @Named("UnAuthApiService") unAuthApiService: ApiService,
    ): AuthInterceptor {
        return AuthInterceptor(authStore, unAuthApiService)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    @Provides
    fun provideMapper(): Mapper {
        return Mapper()
    }

    @Provides
    @Singleton
    fun provideAuthStore(sharedPreferences: SharedPreferences): AuthStore {
        return AuthStoreImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideProfileStore(sharedPreferences: SharedPreferences): ProfileStore {
        return ProfileStoreImpl(sharedPreferences)
    }
}