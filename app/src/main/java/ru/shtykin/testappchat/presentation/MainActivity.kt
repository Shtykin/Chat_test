package ru.shtykin.testappchat.presentation

import android.os.Bundle
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
import ru.shtykin.testappchat.presentation.screen.login.LoginScreen


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
                        LoginScreen(uiState = uiState)
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