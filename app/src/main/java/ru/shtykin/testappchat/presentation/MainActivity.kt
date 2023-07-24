package ru.shtykin.testappchat.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.shtykin.testappchat.navigation.AppNavGraph
import ru.shtykin.testappchat.navigation.Screen
import ru.shtykin.testappchat.presentation.screen.all_chats.AllChatsScreen
import ru.shtykin.testappchat.presentation.screen.choose_country.ChooseCountryScreen
import ru.shtykin.testappchat.presentation.screen.login.LoginScreen
import ru.shtykin.testappchat.presentation.screen.registration.RegistrationScreen
import ru.shtykin.testappchat.presentation.ui.theme.TestAppChatTheme
import ru.shtykin.testappchat.settings.AuthStore
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var authStore: AuthStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                                    Log.e("DEBUG", "registration -> $phone, $name, $username")
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
                                            navHostController.navigate(Screen.AllChats.route)
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
                                uiState = uiState
                            )
                        },
                        chatScreenContent = {

                        },
                        profileScreenContent = {

                        },
                        editProfileScreenContent = {

                        }
                    )
                }
            }
        }
    }
}