package com.example.chronos.data.models

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val title: String,
    val description: String,
)