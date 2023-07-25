package ru.shtykin.testappchat.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.shtykin.testappchat.domain.entity.Profile
import ru.shtykin.testappchat.navigation.AppNavGraph
import ru.shtykin.testappchat.navigation.Screen
import ru.shtykin.testappchat.presentation.screen.all_chats.AllChatsScreen
import ru.shtykin.testappchat.presentation.screen.choose_country.ChooseCountryScreen
import ru.shtykin.testappchat.presentation.screen.edit_profile.EditProfileScreen
import ru.shtykin.testappchat.presentation.screen.login.LoginScreen
import ru.shtykin.testappchat.presentation.screen.profile.ProfileScreen
import ru.shtykin.testappchat.presentation.screen.registration.RegistrationScreen
import ru.shtykin.testappchat.presentation.ui.theme.TestAppChatTheme
import ru.shtykin.testappchat.settings.AuthStore
import ru.shtykin.testappchat.settings.ProfileStore
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var authStore: AuthStore

    @Inject
    lateinit var profileStore: ProfileStore

    private var avatarUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contractAvatarImage = ActivityResultContracts.GetContent()
        val launchAvatarImage = registerForActivityResult(contractAvatarImage) { uri ->
            uri?.let { viewModel.updateBmp(mapUriToBase64(it)) }

        }
        setContent {
            val navHostController = rememberNavController()
            val scope = rememberCoroutineScope()
            val uiState by viewModel.uiState
            val startScreenRoute =
                if (authStore.isAuthenticated()) Screen.AllChats.route else Screen.Login.route

            TestAppChatTheme(

            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavGraph(
                        startScreenRoute = startScreenRoute,
                        navHostController = navHostController,
                        registrationScreenContent = {
                            RegistrationScreen(
                                uiState = uiState,
                                onRegistrationClick = { phone, name, username ->
                                    viewModel.tryToRegister(phone, name, username) {
                                        navHostController.popBackStack()
                                        Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                                    }
                                },
                                onBackClick = {
                                    navHostController.popBackStack()
                                    viewModel.loginScreenOpened(it)
                                }
                            )
                        },
                        loginScreenContent = {
                            LoginScreen(
                                uiState = uiState,
                                getFlagEmoji = { viewModel.getFlagEmoji(it) },
                                onEmojiClick = {
                                    navHostController.navigate(Screen.ChooseCountry.route)
                                    viewModel.chooseCountryScreenOpened()
                                },
                                onRegistrationClick = {

                                    viewModel.registrationScreenOpened(
                                        phone = it,
                                        onParseSuccess = { navHostController.navigate(Screen.Registration.route) }
                                    )
                                },
                                onRequestSmsClick = {
                                    viewModel.tryToRequestSms(it)
                                },
                                onLoginClick = { phone, code ->
                                    viewModel.tryToLogin(
                                        phone = phone,
                                        code = code,
                                        onSuccess = {
                                            navHostController.navigate(Screen.AllChats.route) {
                                                popUpTo(Screen.Login.route) {inclusive = true}
                                            }
                                        }
                                    )
                                }
                            )
                        },
                        chooseCountryScreenContent = {
                            ChooseCountryScreen(
                                uiState = uiState,
                                onCountryClick = {
                                    navHostController.popBackStack()
                                    viewModel.loginScreenOpened(it)
                                },
                                onBackClick = { navHostController.popBackStack() }
                            )
                        },
                        allChatsScreenContent = {
                            AllChatsScreen(
                                uiState = uiState,
                                onProfileClick = {
                                    navHostController.navigate(Screen.Profile.route){
                                        popUpTo(Screen.AllChats.route)
                                    }
                                    viewModel.profileScreenOpened()
                                }
                            )
                        },
                        chatScreenContent = {

                        },
                        profileScreenContent = {
                            ProfileScreen(
                                uiState = uiState,
                                onEditProfileClick = {
                                    navHostController.navigate(Screen.EditProfile.route)
                                    viewModel.editProfileScreenOpened()
                                },
                                onBackClick = {
                                    navHostController.navigate(Screen.AllChats.route){
                                        popUpTo(Screen.AllChats.route){inclusive = true}
                                    }
                                }
                            )
                        },
                        editProfileScreenContent = {
                            EditProfileScreen(
                                uiState = uiState,
                                onChangeAvatarClick = { launchAvatarImage.launch("image/*") },
                                onSaveClick = {
                                    viewModel.saveProfile(
                                        profile = it,
                                        onSuccess = {navHostController.navigate(Screen.Profile.route)}
                                    )
                                },
                                onCancelClick = {
                                    viewModel.profileScreenOpened()
                                    navHostController.navigate(Screen.Profile.route)
                                }
                            )
                        }
                    )
                }
            }
        }
    }

//    private fun getBase(): Bitmap? {
//        try {
//            avatarUri?.let { uri ->
//                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
//                val outputStream = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//                val byteArray = outputStream.toByteArray()
//                val encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT)
//                val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
//                return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
//            }
//            return null
//        } catch (e: Exception) {
//            return null
//        }
//    }


    private fun saveAvatarToStorage(uri: Uri) {
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(this.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        profileStore.avatar = Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun mapUriToBase64(uri: Uri): String {
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(this.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

}