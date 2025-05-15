package com.example.cashpilot.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.cashpilot.R

val GothamProMedium = FontFamily(
    Font(R.font.gotham_pro_medium, FontWeight.Medium)
)

val GothamProBoldItalic = FontFamily(
    Font(R.font.gotham_pro_bold_italic, FontWeight.Bold)  // Указываем именно Bold Italic
)

val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = GothamProMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = GothamProMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = GothamProMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    titleLarge = TextStyle(
        fontFamily = GothamProMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    )
)
