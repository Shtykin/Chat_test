package ru.shtykin.testappchat.data.network

import retrofit2.Call
import retrofit2.http.*
import ru.shtykin.testappchat.data.network.model.AvatarDto
import ru.shtykin.testappchat.data.network.model.ProfileDataDto
import ru.shtykin.testappchat.data.network.model.RequestCheckAuthCodeDto
import ru.shtykin.testappchat.data.network.model.RequestProfileDto
import ru.shtykin.testappchat.data.network.model.ResponseCheckAuthCodeDto
import ru.shtykin.testappchat.data.network.model.RequestPutProfileDto
import ru.shtykin.testappchat.data.network.model.RequestRefreshTokenDto
import ru.shtykin.testappchat.data.network.model.RequestRegisterDto
import ru.shtykin.testappchat.data.network.model.RequestSendAuthCodeDto
import ru.shtykin.testappchat.data.network.model.ResponseSendAuthCodeDto
import ru.shtykin.testappchat.data.network.model.UserTokensDto


interface ApiService {

    @POST("api/v1/users/register/")
    fun register (
        @Body requestRegisterDto: RequestRegisterDto
    ): Call<UserTokensDto>

    @POST("api/v1/users/send-auth-code/")
    fun sendAuthCode (
        @Body requestSendAuthCodeDto: RequestSendAuthCodeDto,
    ): Call<ResponseSendAuthCodeDto>

    @POST("api/v1/users/check-auth-code/")
    fun checkAuthCode (
        @Body requestCheckAuthCodeDto: RequestCheckAuthCodeDto,
    ): Call<ResponseCheckAuthCodeDto>

    @POST("api/v1/users/refresh-token/")
    fun refreshToken (
        @Body requestRefreshTokenDto: RequestRefreshTokenDto
    ): Call<UserTokensDto>

    @GET("/api/v1/users/me/")
    fun getProfile (
    ): Call<RequestProfileDto>

    @PUT("/api/v1/users/me/")
    fun putProfile (
        @Body requestPutProfileDto: RequestPutProfileDto
    ): Call<RequestPutProfileDto>

}