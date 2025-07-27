package com.example.chronos.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.chronos.ui.components.screens.login.LoginScreen
import com.example.chronos.utils.Constants.LOGIN_SCREEN

fun NavGraphBuilder.loginComposable(
    navController: NavHostController
) {
    composable(route = LOGIN_SCREEN) {
        LoginScreen(navController = navController)
    }
}