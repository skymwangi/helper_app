package com.example.helperapp.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class DashboardStat(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val background: Color
)
data class QuickAction(
    val title: String,
    val icon: ImageVector
)