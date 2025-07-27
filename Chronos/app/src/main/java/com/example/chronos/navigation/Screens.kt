package com.example.chronos.navigation

import androidx.navigation.NavController
import com.example.chronos.utils.Constants.LIST_SCREEN
import com.example.chronos.utils.Constants.LOGIN_SCREEN

class Screens(navController: NavController) {

    val login: () -> Unit = {
        navController.navigate(LOGIN_SCREEN) {
            popUpTo(LOGIN_SCREEN) { inclusive = true }
        }
    }

    val list: () -> Unit = {
        navController.navigate("list"){
            popUpTo(LIST_SCREEN) {inclusive = true}
        }
    }

    val task: (Int) -> Unit = {taskId ->
        navController.navigate("task/$taskId")
    }
}