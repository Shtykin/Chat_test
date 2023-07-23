package ru.shtykin.testappchat.data.repository

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.google.gson.annotations.SerializedName
import retrofit2.http.Field
import ru.shtykin.testappchat.data.mapper.Mapper
import ru.shtykin.testappchat.data.model.AvatarDto
import ru.shtykin.testappchat.data.network.ApiService
import ru.shtykin.testappchat.domain.Repository
import ru.shtykin.testappchat.domain.entity.Profile
import java.util.Locale


class RepositoryImpl(
    private val apiService: ApiService,
    private val authApiService: ApiService,
    private val mapper: Mapper
) : Repository {

    override suspend fun registration(phone: String, name: String, userName: String) {
        val response = apiService.register(phone, name, userName)
        response.execute().body()?.let {
            Log.e("DEBUG", "userTokens -> $it")
        }
        throw IllegalStateException("Response body is empty")
    }

    override suspend fun getSmsCode(phone: String): Boolean {
        val response = apiService.sendAuthCode(phone)
        return response.execute().code() in 200..299
    }

    override suspend fun checkAuthCode(phone: String, code: String): Boolean {
        val response = apiService.checkAuthCode(phone, code)
        response.execute().body()?.let {
            if (it.isUserExist) return true
        }
        throw IllegalStateException("Response body is empty")
    }

    override suspend fun getProfile(): Profile {
        val response = apiService.getProfile()
        response.execute().body()?.let {
            return mapper.mapProfileDataDtoToProfile(it)
        }
        throw IllegalStateException("Response body is empty")
    }

    override suspend fun putProfile(
        name: String,
        birthday: String,
        city: String,
        vk: String,
        instagram: String,
        status: String,
        avatarFilename: String,
        avatarBase64: String,
    ) {
        val avatarDto = AvatarDto(
            filename = avatarFilename,
            base64 = avatarBase64
        )
        val response = apiService.putProfile(
            name = name,
            birthday = birthday,
            city = city,
            vk = vk,
            instagram = instagram,
            status = status,
            avatar = avatarDto
        )
    }
}