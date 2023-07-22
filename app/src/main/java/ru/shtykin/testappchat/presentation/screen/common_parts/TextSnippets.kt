package ru.shtykin.testappchat.presentation.screen.common_parts

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.googlefonts.GoogleFont
import ru.shtykin.testappchat.R

@Composable
fun sno() {
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )
}