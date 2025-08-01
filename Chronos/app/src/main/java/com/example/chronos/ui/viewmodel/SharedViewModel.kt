package com.example.chronos.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chronos.data.models.Priority
import com.example.chronos.data.models.ToDoTask
import com.example.chronos.data.repos.DataStoreRepository
import com.example.chronos.data.repos.TodoRepository
import com.example.chronos.utils.Constants.MAX_TITLE_LENGTH
import com.example.chronos.utils.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repo: TodoRepository,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    val id: MutableState<Int> = mutableIntStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)
    val date: MutableState<String> = mutableStateOf("")
    val time: MutableState<String> = mutableStateOf("")
    val image: MutableState<String> = mutableStateOf("")

    val searchTextState: MutableState<String> =  mutableStateOf("")

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks : StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    private val _searchTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchTasks : StateFlow<RequestState<List<ToDoTask>>> = _searchTasks

    val lowPriorityTasks: StateFlow<List<ToDoTask>> =
        repo.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val highPriorityTasks: StateFlow<List<ToDoTask>> =
        repo.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState : StateFlow<RequestState<Priority>> = _sortState

    fun readSortState(){
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState
                    .map {
                        Priority.valueOf(it)
                    }
                    .collect{
                        _sortState.value = RequestState.Success(it)
                    }
            }
        }catch (e: Exception){
            _sortState.value = RequestState.Error(e)
        }
    }

    fun persistSortState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(priority)
        }
    }

    fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repo.getAllTasks.collect{
                    _allTasks.value = RequestState.Success(it)
                }
            }
        }catch (e: Exception){
            _allTasks.value = RequestState.Error(e)
        }
    }

    fun getSearchTasks(query: String) {
        _searchTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repo.searchDatabase("%$query%").collect{
                    _searchTasks.value = RequestState.Success(it)
                }
            }
        }catch (e: Exception){
            _searchTasks.value = RequestState.Error(e)
        }
    }

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    fun getSelectedTask(taskId: Int){
        viewModelScope.launch {
            repo.getSelectedTask(taskId).collect{task ->
                _selectedTask.value = task
            }
        }
    }

    fun addTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title.value,
                description = description.value,
                priority = priority.value,
                date = date.value,
                time = time.value,
                image = image.value
            )
            repo.addTask(toDoTask)
        }
    }

    fun updateTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value,
                date = date.value,
                time = time.value,
                image = image.value
            )
            repo.updateTask(toDoTask)
        }
    }

    fun deleteTask(toDoTask: ToDoTask){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteTask(toDoTask)
            repo.getAllTasks
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO)  {
            repo.deleteAllTasks()
            repo.getAllTasks
        }
    }

    fun updateTaskFields(selectedTask: ToDoTask?) {
        if( selectedTask != null){
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
            date.value = selectedTask.date
            time.value = selectedTask.time
            image.value = selectedTask.image.toString()
        }
        else {
            id.value =0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
            date.value = ""
            time.value = ""
            image.value = ""
        }
    }

    fun updateTitle(newTitle: String){
        if(newTitle.length < MAX_TITLE_LENGTH){
            title.value = newTitle
        }
    }

    fun updateDate(newDate: String) {
        date.value = newDate
    }

    fun updateTime(newTime: String) {
        time.value = newTime
    }

    fun updateImage(newImage: String){
        image.value = newImage
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }

    fun validateDelete(): Boolean {
        return title.value.isNotEmpty() or description.value.isNotEmpty()
    }
}