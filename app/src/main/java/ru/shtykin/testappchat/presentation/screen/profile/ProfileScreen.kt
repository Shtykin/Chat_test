package ru.shtykin.testappchat.presentation.screen.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.shtykin.testappchat.presentation.screen.common_parts.VerticalSpace
import ru.shtykin.testappchat.presentation.state.ScreenState


@Composable
fun ProfileScreen(
    uiState: ScreenState,
    onGetPictureClick: (() -> Unit)?,
    onGetBase64Click: (() -> Unit)?,
) {
    val profile = (uiState as? ScreenState.ProfileScreen)?.profile
    var imageUri by remember { mutableStateOf(Uri.EMPTY) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            imageUri = it
            Log.e("DEBUG1", "imageUri -> $imageUri")
        }
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "ПРОФИЛЬ СКРИН")
            Button(onClick = { launcher.launch("image/*") }) {
                Text(text = "Выбрать изображение")
            }
            AsyncImage(
                modifier = Modifier.size(250.dp),
                model = imageUri,
                contentDescription = null
            )
            VerticalSpace(height = 16.dp)



            Button(onClick = { onGetPictureClick?.invoke() }) {
                Text(text = "Выбрать изображение2")
            }
            Button(onClick = { onGetBase64Click?.invoke() }) {
                Text(text = "Преобразовать")
            }
            VerticalSpace(height = 16.dp)
            profile?.avatar?.let {
                AsyncImage(
                    modifier = Modifier.size(250.dp),
                    model = it,
                    contentDescription = null
                )
            }

//            ImageSelectionScreen()

//            Image(
//                bitmap = ,
//                contentDescription = null
//            )

        }
    }

}

@Composable
fun ImageSelectionScreen() {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { saveImageToInternalStorage(context, it) }
        }
    )

    Button(onClick = { launcher.launch("image/*") }) {
        Text(text = "Select Image")
    }
}

fun saveImageToInternalStorage(context: Context, uri: Uri) {
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = context.openFileOutput("image.jpg", Context.MODE_PRIVATE)
    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
}