package ru.shtykin.testappchat.data.mapper

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
        avatar = profileDataDto.avatar
    )

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