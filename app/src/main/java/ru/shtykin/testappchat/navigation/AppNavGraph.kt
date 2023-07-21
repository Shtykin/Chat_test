package ru.shtykin.testappchat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    startScreenRoute: String,
    navHostController: NavHostController,
    registrationScreenContent: @Composable () -> Unit,
    loginScreenContent: @Composable () -> Unit,
    allChatsScreenContent: @Composable () -> Unit,
    chatScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit,
    editProfileScreenContent: @Composable () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = startScreenRoute
    ) {
        composable(Screen.Registration.route) {
            registrationScreenContent()
        }
        composable(Screen.Login.route) {
            loginScreenContent()
        }
        composable(Screen.AllChats.route) {
            allChatsScreenContent()
        }
        composable(Screen.Chat.route) {
            chatScreenContent()
        }
        composable(Screen.Profile.route) {
            profileScreenContent()
        }
        composable(Screen.EditProfile.route) {
            editProfileScreenContent()
        }
    }

}