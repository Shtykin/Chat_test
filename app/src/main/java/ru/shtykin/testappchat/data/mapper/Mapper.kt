package ru.shtykin.testappchat.data.mapper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import ru.shtykin.testappchat.data.network.model.ProfileDataDto
import ru.shtykin.testappchat.data.network.model.ResponseCheckAuthCodeDto
import ru.shtykin.testappchat.data.network.model.UserTokensDto
import ru.shtykin.testappchat.domain.entity.Profile
import ru.shtykin.testappchat.domain.entity.UserTokens

class Mapper {

    fun mapProfileDataDtoToProfile(profileDataDto: ProfileDataDto) = Profile(
        name = profileDataDto.name,
        username = profileDataDto.username,
        birthday = profileDataDto.birthday,
        city = profileDataDto.city,
        avatar = mapBase64ToBitmap(profileDataDto.avatar)
    )

    private fun mapBase64ToBitmap(base64: String): Bitmap {
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun mapUserTokensDtoToUserTokens(userTokensDto: UserTokensDto) = UserTokens(
        accessToken = userTokensDto.accessToken,
        refreshToken = userTokensDto.refreshToken,
        userId = userTokensDto.userId,
    )

    fun mapResponseCheckAuthCodeDtoToUserTokens(responseCheckAuthCodeDto: ResponseCheckAuthCodeDto) =
        UserTokens(
            accessToken = responseCheckAuthCodeDto.accessToken,
            refreshToken = responseCheckAuthCodeDto.refreshToken,
            userId = responseCheckAuthCodeDto.userId,
        )

}