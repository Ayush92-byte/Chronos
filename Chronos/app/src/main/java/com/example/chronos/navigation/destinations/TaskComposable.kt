package com.example.chronos.navigation.destinations

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.chronos.ui.components.screens.task.TaskScreen
import com.example.chronos.ui.viewmodel.SharedViewModel
import com.example.chronos.utils.Constants.TASK_ARGUMENT_KEY
import com.example.chronos.utils.Constants.TASK_SCREEN

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.taskComposable(
    sharedViewModel: SharedViewModel,
    navController: NavController
) {
    composable(
        route = TASK_SCREEN,
        arguments = listOf(navArgument(TASK_ARGUMENT_KEY){
            type = NavType.IntType
        })
    ){ navBackStackEntry ->
        val taskId = navBackStackEntry.arguments!!.getInt(TASK_ARGUMENT_KEY)

        LaunchedEffect(taskId){
            sharedViewModel.getSelectedTask(taskId)
        }

        val selectedTask by sharedViewModel.selectedTask.collectAsState()

        LaunchedEffect(selectedTask){
            if(selectedTask != null || taskId == -1){
                sharedViewModel.updateTaskFields(selectedTask)
            }
        }
        TaskScreen( selectedTask, sharedViewModel, navController )
    }
}