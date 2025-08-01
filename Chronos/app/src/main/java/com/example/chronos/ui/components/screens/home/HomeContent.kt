package com.example.chronos.ui.components.screens.home

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ContentAlpha
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.chronos.R
import com.example.chronos.data.models.Priority
import com.example.chronos.data.models.ToDoTask
import com.example.chronos.ui.theme.LARGE_PADDING
import com.example.chronos.ui.theme.MEDIUM_PADDING
import com.example.chronos.ui.theme.TASK_ITEM_ELEVATION
import com.example.chronos.ui.viewmodel.SharedViewModel
import com.example.chronos.utils.RequestState

@Composable
fun ListContent(
    tasks: RequestState<List<ToDoTask>>,
    searchedTasks: RequestState<List<ToDoTask>>,
    lowPriority: List<ToDoTask>,
    highPriority: List<ToDoTask>,
    sortState: RequestState<Priority>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    context: Context,
    sharedViewModel: SharedViewModel
) {
    if(sortState is RequestState.Success){
        when{
            searchedTasks is RequestState.Success -> {
                HandleListContent(
                    tasks = searchedTasks.data,
                    navigateToTaskScreen = navigateToTaskScreen,
                    context,
                    sharedViewModel
                )
            }

            sortState.data == Priority.NONE -> {
                if(tasks is RequestState.Success){
                    HandleListContent(
                        tasks = tasks.data ,
                        navigateToTaskScreen = navigateToTaskScreen,
                        context,
                        sharedViewModel
                    )
                }
            }

            sortState.data == Priority.LOW -> {
                HandleListContent(
                    tasks = lowPriority,
                    navigateToTaskScreen = navigateToTaskScreen,
                    context,
                    sharedViewModel
                )
            }
            sortState.data == Priority.HIGH -> {
                HandleListContent(
                    tasks = highPriority,
                    navigateToTaskScreen = navigateToTaskScreen,
                    context,
                    sharedViewModel
                )
            }
        }
    }
}

@Composable
fun DisplayTasks(
    tasks: List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    context: Context,
    sharedViewModel: SharedViewModel
) {
    LazyColumn{
        items(tasks){task ->
            TaskItem(task, navigateToTaskScreen, context, sharedViewModel )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    context: Context,
    sharedViewModel: SharedViewModel
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = MEDIUM_PADDING),
        color = colorResource(id = R.color.taskColor),
        shape = MaterialTheme.shapes.extraSmall,
        shadowElevation = TASK_ITEM_ELEVATION,
        onClick = {navigateToTaskScreen(toDoTask.id)}
    ){
        Row(modifier = Modifier.fillMaxWidth()){
            Column(modifier = Modifier.weight(1f)) {
                Canvas(modifier = Modifier.size(11.dp, 120.dp)
                ){
                    drawRect(
                        color = toDoTask.priority.color
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(all = LARGE_PADDING)
                    .fillMaxWidth()
                    .weight(20f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.weight(8f),
                        text = toDoTask.title,
                        color = colorResource(id = R.color.textcolor),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    if(toDoTask.image?.isNotEmpty() == true){
                        GlideImage(
                            modifier = Modifier.size(36.dp).clip(CircleShape),
                            model = toDoTask.image,
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                    }
                    IconButton(onClick = {
                        sharedViewModel.deleteTask(toDoTask)
                    }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "",
                                tint = colorResource(id = R.color.textcolor)
                            )
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Text(
                        modifier = Modifier.weight(8f),
                        text = toDoTask.description,
                        color = colorResource(id = R.color.textcolor),
                        maxLines = 2
                    )
                    IconButton(
                        onClick = {

                            val pm = context.packageManager
                            val appInfo: ApplicationInfo? = try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    pm.getApplicationInfo(
                                        "com.whatsapp",
                                        PackageManager.ApplicationInfoFlags.of(0)
                                    )
                                } else {
                                    pm.getApplicationInfo("com.whatsapp", 0)
                                }
                            } catch (e: PackageManager.NameNotFoundException) {
                                null
                            }

                            if (appInfo != null) {
                                val intent = Intent(ACTION_SEND)
                                intent.putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Note Title: ${toDoTask.title}\n\n" +
                                            "Description: ${toDoTask.description}\n\n"
                                )
                                if (!toDoTask.image.isNullOrEmpty()) {
                                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(toDoTask.image))
                                }
                                intent.setPackage("com.whatsapp")
                                intent.setType("text/plain")
                                context.startActivity(intent)
                            } else {
                                Toast.makeText(context, "Please install WhatsApp first.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.TopEnd
                        ){
                            Icon(
                                painterResource(id = R.drawable.ic_send),
                                modifier = Modifier.size(20.dp),
                                contentDescription = "",
                                tint= colorResource(id = R.color.textcolor)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HandleListContent(
    tasks:List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    context: Context,
    sharedViewModel: SharedViewModel
) {
    if(tasks.isEmpty()){
        EmptyContent()
    }
    DisplayTasks(
        tasks = tasks,
        navigateToTaskScreen = navigateToTaskScreen,
        context,
        sharedViewModel
    )
}

@Composable
fun SearchView(
    text: String,
    onTextChanged: (String) -> Unit,
    onCloseClicked: () -> Unit,
    sharedViewModel: SharedViewModel,
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(all = MEDIUM_PADDING)
        ,
        value = text,
        onValueChange = {
            onTextChanged(it)
            sharedViewModel.getSearchTasks(query = it)
        },
        placeholder = {
            Text(
                modifier = Modifier.alpha(ContentAlpha.medium),
                text= stringResource(id = R.string.search_placeholder),
                color = colorResource(id = R.color.textcolor)
            ) },
        textStyle =
        TextStyle(
            color = colorResource(id = R.color.textcolor),
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        ),
        singleLine = true,
        leadingIcon = {
            Icon(
                modifier= Modifier.alpha(ContentAlpha.disabled),
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(id = R.string.search_icon),
                tint = colorResource(id = R.color.textcolor)
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    onTextChanged("")
                    onCloseClicked()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(id = R.string.close_icon),
                    tint = colorResource(id = R.color.textcolor)
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = colorResource(id = R.color.fabColor),
            focusedContainerColor = colorResource(id = R.color.fabColor)
        )
    )
}