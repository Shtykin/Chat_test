package ru.shtykin.testappchat.data.mapper

import ru.shtykin.testappchat.data.model.ProfileDataDto
import ru.shtykin.testappchat.domain.entity.Profile

class Mapper {

    fun mapProfileDataDtoToProfile(profileDataDto: ProfileDataDto) = Profile(
        name = profileDataDto.name,
        username = profileDataDto.username,
        birthday = profileDataDto.birthday,
        city = profileDataDto.city,
        avatar = profileDataDto.avatar
    )

}