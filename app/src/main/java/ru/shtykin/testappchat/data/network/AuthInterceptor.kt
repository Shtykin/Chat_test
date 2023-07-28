package ru.shtykin.testappchat.data.network

import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.shtykin.testappchat.data.network.model.RequestRefreshTokenDto
import ru.shtykin.testappchat.settings.AuthStore

class AuthInterceptor(
    private val authStore: AuthStore,
    private val unAuthApiService: ApiService
) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            val request = chain.request().appendToken()
            val response = chain.proceed(request)
            return try {
                if (response.code == 401) {
                    runBlocking {
                        val responseBody = unAuthApiService.refreshToken(RequestRefreshTokenDto(authStore.refreshToken)).execute()
                        responseBody.body()?.let {
                            authStore.apply {
                                this.phone = phone
                                this.accessToken = it.accessToken
                                this.refreshToken = it.refreshToken
                            }
                            return@runBlocking
                        }
                        responseBody.errorBody()?.string()?.let { throw IllegalStateException(parseError(it)) }
                        throw IllegalStateException("Response body is empty")
                    }
                    response.close()
                    chain.proceed(request.appendToken())
                } else {
                    response
                }
            } catch (e: Throwable) {
                response
            }
        }
    }

    private fun Request.appendToken(): Request {
        val authHeaderName = "Authorization"
        return newBuilder()
            .removeHeader(authHeaderName)
            .addHeader(authHeaderName, "Bearer " + authStore.accessToken)
            .build()
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