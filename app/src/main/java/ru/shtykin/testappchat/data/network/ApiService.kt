package ru.shtykin.testappchat.data.network

import retrofit2.Call
import retrofit2.http.*
import ru.shtykin.testappchat.data.network.model.AvatarDto
import ru.shtykin.testappchat.data.network.model.ProfileDataDto
import ru.shtykin.testappchat.data.network.model.RequestCheckAuthCodeDto
import ru.shtykin.testappchat.data.network.model.ResponseCheckAuthCodeDto
import ru.shtykin.testappchat.data.network.model.RequestPutProfileDto
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

    @GET("/api/v1/users/me/")
    fun getProfile (
    ): Call<ProfileDataDto>

    @PUT("/api/v1/users/me/")
    fun putProfile (
        @Field("name") name: String,
//        @Field("username") username: String,
        @Field("birthday") birthday: String,
        @Field("city") city: String,
        @Field("vk") vk: String,
        @Field("instagram") instagram: String,
        @Field("status") status: String,
        @Field("avatar") avatar: AvatarDto,
    ): Call<RequestPutProfileDto>

}