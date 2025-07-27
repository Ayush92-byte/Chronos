package com.example.chronos.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.chronos.utils.Constants.DATABASE_TABLE

@Entity(tableName = DATABASE_TABLE)
data class ToDoTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val description: String,
    val priority: Priority,
    val date: String,
    val time: String,
    val image: String?
)