package com.example.cashpilot.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cashpilot.R
import com.example.cashpilot.ui.components.Header
import com.example.cashpilot.viewmodel.MainViewModel

@Composable
fun MainScreen(navController: NavController, userName: String, viewModel: MainViewModel = viewModel()) {

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF202020))
        .padding(24.dp)) {

        Header(userName = userName)


            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF53B63A)),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 361.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Что Вас интересует?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)
                            .padding(bottom = 21.dp)
                    )

                    OptionButton(
                        text = "Предложения по вкладам",
                        onClick = { navController.navigate("deposit/$userName") }
                    )

                    Spacer(modifier = Modifier.height(21.dp))

                    OptionButton(
                        text = "Предложения по кредитам",
                        onClick = { navController.navigate("credit/$userName") }
                    )

                    Spacer(modifier = Modifier.height(21.dp))

                    OptionButton(
                        text = "Расчет по вашим данным",
                        onClick = { navController.navigate("calculator/$userName") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Предложения для Вас",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Пример предложений
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Лучший % без снятия до 23%",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "от 100 000 ₽",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }


@Composable
fun OptionButton(
    text: String,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0x202020).copy(alpha = 0.14f)),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier
            .width(304.dp) // ширина 304dp
            .height(75.dp) // высота 75dp
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            ) {
                // Тут будет иконка
            }
        }
    }
}

