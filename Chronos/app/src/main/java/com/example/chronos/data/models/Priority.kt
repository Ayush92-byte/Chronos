package com.example.chronos.data.models

import androidx.compose.ui.graphics.Color
import com.example.chronos.ui.theme.HighPriorityColor
import com.example.chronos.ui.theme.LowPriorityColor
import com.example.chronos.ui.theme.MediumPriorityColor
import com.example.chronos.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}