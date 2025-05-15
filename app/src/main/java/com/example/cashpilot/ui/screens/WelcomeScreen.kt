package com.example.cashpilot.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cashpilot.R
import com.example.cashpilot.ui.components.GreenButton

@Composable
fun WelcomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.92f))
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_dollar),
            contentDescription = "Dollar Background",
            modifier = Modifier
                .width(724.dp)
                .height(724.dp)
                .align(Alignment.Center)
                .graphicsLayer { alpha = 0.25f },
            contentScale = ContentScale.Crop
        )


    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Добро пожаловать\nв CashPilot!",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = "Чтобы получать персонализированные\nвыгодные предложения от банков,\nнужно авторизоваться",
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(bottom = 48.dp)
            )

        GreenButton(
            text = "Зарегистрироваться",
            onClick = { navController.navigate("register") },
            fontSize = 30,
            modifier = Modifier
                .fillMaxWidth()
        )

    }

        GreenButton(
            text = "Войти",
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(navController = rememberNavController())
}
