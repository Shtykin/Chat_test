package ru.shtykin.testappchat.data.repository

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.http.Field
import ru.shtykin.testappchat.data.mapper.Mapper
import ru.shtykin.testappchat.data.network.model.AvatarDto
import ru.shtykin.testappchat.data.network.model.RequestRegisterDto
import ru.shtykin.testappchat.data.network.ApiService
import ru.shtykin.testappchat.data.network.ErrorResponse
import ru.shtykin.testappchat.data.network.ErrorValidation
import ru.shtykin.testappchat.data.network.model.RequestCheckAuthCodeDto
import ru.shtykin.testappchat.data.network.model.RequestSendAuthCodeDto
import ru.shtykin.testappchat.domain.Repository
import ru.shtykin.testappchat.domain.entity.Profile
import ru.shtykin.testappchat.domain.entity.UserTokens
import java.util.Locale


class RepositoryImpl(
    private val apiService: ApiService,
    private val authApiService: ApiService,
    private val mapper: Mapper
) : Repository {

    override suspend fun registration(phone: String, name: String, userName: String): UserTokens {
        val response = apiService.register(RequestRegisterDto(phone, name, userName)).execute()
        response.body()?.let { return mapper.mapUserTokensDtoToUserTokens(it) }
        response.errorBody()?.string()?.let { throw IllegalStateException(parseError(it)) }
        throw IllegalStateException("Response body is empty")
    }


    override suspend fun sendAuthCode(phone: String): Boolean {
        val response = apiService.sendAuthCode(RequestSendAuthCodeDto(phone)).execute()
        response.body()?.let {
            if (it.isSuccess) return true
            else throw IllegalStateException("Смс не была отправлена")
        }
        response.errorBody()?.string()?.let { throw IllegalStateException(parseError(it)) }
        throw IllegalStateException("Response body is empty")
    }

    override suspend fun checkAuthCode(phone: String, code: String): UserTokens {
        val response = apiService.checkAuthCode(RequestCheckAuthCodeDto(phone, code)).execute()
        response.body()?.let {
            if (it.isUserExist) return mapper.mapResponseCheckAuthCodeDtoToUserTokens(it)
        }
        response.errorBody()?.string()?.let { throw IllegalStateException(parseError(it)) }
        throw IllegalStateException("Response body is empty")
    }

    override suspend fun getProfile(): Profile {
        val response = authApiService.getProfile()
        response.execute().body()?.let {
            Log.e("DEBUG1", "request -> $it")
            return mapper.mapProfileDataDtoToProfile(it)
        }
        throw IllegalStateException("Response body is empty")
    }

    override suspend fun putProfile(profile: Profile): Boolean {
        val response = authApiService.putProfile(
            mapper.mapProfileToRequestPutProfileDto(profile)
        ).execute()
        if (response.code() == 200) return true
        response.errorBody()?.string()?.let { throw IllegalStateException(parseError(it)) }
        throw IllegalStateException("Response body is empty")
    }

    private fun parseError(errorMessage: String): String? {
        val gson = Gson()
        var message: String? = try {
            val errorResponse = gson.fromJson(errorMessage, ErrorResponse::class.java)
            errorResponse.detail.message
        } catch (e: Exception) {
            null
        }
        if (message != null) return message

        message = try {
            val errorValidation = gson.fromJson(errorMessage, ErrorValidation::class.java)
            errorValidation.detail[0].msg
        } catch (e: Exception) {
            null
        }
        return message
    }
}