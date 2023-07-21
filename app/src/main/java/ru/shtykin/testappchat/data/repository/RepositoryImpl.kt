package ru.shtykin.testappchat.data.repository

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import ru.shtykin.testappchat.domain.Repository
import java.util.Locale


class RepositoryImpl(
    private val context: Context
) : Repository {
    override fun getCurrentRegion(): String {
        return try {
//            context.resources.configuration.locales[0].country
            Locale.getDefault().country
        } catch (e: Exception) {
            throw RuntimeException("Error while get current location")
        }
    }
}