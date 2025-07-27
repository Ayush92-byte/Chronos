package com.example.chronos.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.chronos.ui.components.screens.home.HomeScreen
import com.example.chronos.ui.viewmodel.SharedViewModel
import com.example.chronos.utils.Constants.LIST_ARGUMENT_KEY
import com.example.chronos.utils.Constants.LIST_SCREEN

fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel,
) {
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY){
            type = NavType.StringType
        })
    ){
        HomeScreen(navigateToTaskScreen = navigateToTaskScreen, sharedViewModel)
    }
}