package ru.shtykin.testappchat.data.network

import retrofit2.Call
import retrofit2.http.*
import ru.shtykin.testappchat.data.model.AvatarDto
import ru.shtykin.testappchat.data.model.ProfileDataDto
import ru.shtykin.testappchat.data.model.RequestCheckAuthCodeDto
import ru.shtykin.testappchat.data.model.RequestPutProfileDto
import ru.shtykin.testappchat.data.model.UserTokensDto


interface ApiService {

    @POST("api/v1/users/register/")
    fun register (
        @Field("phone") phone: String,
        @Field("name") name: String,
        @Field("username") username: String,
    ): Call<UserTokensDto>

    @POST("api/v1/users/send-auth-code/")
    fun sendAuthCode (
        @Field("phone") phone: String,
    ): Call<Unit>

    @POST("api/v1/users/check-auth-code/")
    fun checkAuthCode (
        @Field("phone") phone: String,
        @Field("code") code: String,
    ): Call<RequestCheckAuthCodeDto>

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