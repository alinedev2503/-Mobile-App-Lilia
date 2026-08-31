package com.example.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Lília Personal Diet - Shapes & Radii System
val LiliaShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),   // sm: 0.25rem (4px)
    small = RoundedCornerShape(8.dp),        // DEFAULT: 0.5rem (8px) - Inputs & small containers
    medium = RoundedCornerShape(12.dp),      // md: 0.75rem (12px)
    large = RoundedCornerShape(16.dp),       // lg: 1rem (16px) - Content cards & food imagery
    extraLarge = RoundedCornerShape(24.dp)   // xl: 1.5rem (24px) - Modals & sheets
)

val PillShape = RoundedCornerShape(9999.dp)
val CardShape = RoundedCornerShape(16.dp)
val InputShape = RoundedCornerShape(8.dp)
val ChipShape = RoundedCornerShape(9999.dp)
val CircularShape = CircleShape
