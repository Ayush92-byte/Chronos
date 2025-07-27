package com.example.chronos.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.chronos.navigation.destinations.listComposable
import com.example.chronos.navigation.destinations.loginComposable
import com.example.chronos.navigation.destinations.taskComposable
import com.example.chronos.ui.viewmodel.SharedViewModel
import com.example.chronos.utils.Constants.LIST_SCREEN
import com.example.chronos.utils.Constants.LOGIN_SCREEN

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,

    ){
    val screen = remember(navController){
        Screens(navController)
    }

    NavHost(navController = navController, startDestination = LOGIN_SCREEN){
        loginComposable(
            navController
        )
        listComposable(
            screen.task, sharedViewModel
        )
        taskComposable(
            sharedViewModel,
            navController,
        )
    }
}