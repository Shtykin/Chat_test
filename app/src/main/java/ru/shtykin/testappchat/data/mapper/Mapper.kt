package ru.shtykin.testappchat.data.mapper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import ru.shtykin.testappchat.constants.Constants
import ru.shtykin.testappchat.data.network.model.AvatarDto
import ru.shtykin.testappchat.data.network.model.AvatarsDto
import ru.shtykin.testappchat.data.network.model.RequestProfileDto
import ru.shtykin.testappchat.data.network.model.RequestPutProfileDto
import ru.shtykin.testappchat.data.network.model.ResponseCheckAuthCodeDto
import ru.shtykin.testappchat.data.network.model.UserTokensDto
import ru.shtykin.testappchat.domain.entity.Profile
import ru.shtykin.testappchat.domain.entity.UserTokens
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Mapper {

    fun mapProfileDataDtoToProfile(requestProfileDto: RequestProfileDto) = Profile(
        phone = requestProfileDto.profileDataDto.phone,
        name = requestProfileDto.profileDataDto.name,
        username = requestProfileDto.profileDataDto.username,
        birthday = requestProfileDto.profileDataDto.birthday,
        zodiacSign = getZodiacSign(requestProfileDto.profileDataDto.birthday),
        age = getAge(requestProfileDto.profileDataDto.birthday),
        city = requestProfileDto.profileDataDto.city ?: "",
        avatar = null,
        avatarUrl = getAvatarUrl(requestProfileDto.profileDataDto.avatarsDto),
        status = requestProfileDto.profileDataDto.status
    )

    private fun getAvatarUrl(avatarsDto: AvatarsDto?): String? {
        if (avatarsDto == null) return null
        return Constants.baseUrl + avatarsDto.bigAvatar
    }
    private fun getZodiacSign(birthday: String?): String {
        if (birthday == null) return ""
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = try {
            sdf.parse(birthday)
        } catch (e: Exception) {
            return ""
        }
        val day = date.day
        val zodiacSign = when (date.month) {
            1 -> if (day <= 20) "Козерог" else "Водолей"
            2 -> if (day <= 19) "Водолей" else "Рыбы"
            3 -> if (day <= 20) "Рыбы" else "Овен"
            4 -> if (day <= 20) "Овен" else "Телец"
            5 -> if (day <= 20) "Телец" else "Близнецы"
            6 -> if (day <= 21) "Близнецы" else "Рак"
            7 -> if (day <= 22) "Рак" else "Лев"
            8 -> if (day <= 23) "Лев" else "Дева"
            9 -> if (day <= 23) "Дева" else "Весы"
            10 -> if (day <= 23) "Весы" else "Скорпион"
            11 -> if (day <= 22) "Скорпион" else "Стрелец"
            12 -> if (day <= 21) "Стрелец" else "Козерог"
            else -> ""
        }
        return zodiacSign
    }

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

    fun mapProfileToRequestPutProfileDto(profile: Profile) = RequestPutProfileDto(
        name = profile.name,
        username = profile.username,
        birthday = profile.birthday,
//        birthday = profile.birthday,
        city = profile.city,
        vk = null,
        instagram = null,
        status = profile.status,
        avatar = getAvatarDto(profile)
    )

    private fun getAvatarDto(profile: Profile): AvatarDto? {
        return if (profile.avatar == null) null
        else AvatarDto(
            filename = "${profile.username}_avatar",
            base64 = mapBitmapToBase64(profile.avatar)
        )
    }
    private fun getAge(birthday:String): Int? {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val birthdayTimestamp = try {
            sdf.parse(birthday)?.time
        } catch (e: Exception) {
            return null
        } ?: return null
        val currentDate = System.currentTimeMillis()
        return Date(currentDate).year - Date(birthdayTimestamp).year
    }

    private fun mapBitmapToBase64(bitmap: Bitmap?): String? {
        if (bitmap == null) return null
        return try {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val byteArray = outputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            null
        }
    }

}