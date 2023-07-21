package ru.shtykin.testappchat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.shtykin.testappchat.R
import ru.shtykin.testappchat.navigation.AppNavGraph
import ru.shtykin.testappchat.navigation.Screen
import ru.shtykin.testappchat.presentation.screen.choose_country.ChooseCountryScreen
import ru.shtykin.testappchat.presentation.screen.login.LoginScreen
import ru.shtykin.testappchat.presentation.ui.theme.TestAppChatTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            val scope = rememberCoroutineScope()
            val uiState by viewModel.uiState
            val startScreenRoute = Screen.Login.route



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
                                    navHostController.navigate(Screen.Registration.route)
                                    viewModel.registrationScreenOpened()
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