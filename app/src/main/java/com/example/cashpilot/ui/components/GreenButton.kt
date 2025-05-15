package com.example.cashpilot.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashpilot.ui.theme.GothamProMedium

@Composable
fun GreenButton(
    text: String,
    onClick: () -> Unit,
    fontSize: Int = 24,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF53B63A),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFF77C570),
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(30.dp),
        contentPadding = PaddingValues(
            start = 12.dp,
            end = 12.dp,
            top = 6.dp,
            bottom = 6.dp
        ),
        modifier = modifier.heightIn(min = 56.dp)
    ) {
        Text(
            text = text,
            fontFamily = GothamProMedium,
            fontWeight = FontWeight.Medium,
            fontSize = fontSize.sp
        )
    }
}
