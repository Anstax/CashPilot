package com.example.cashpilot.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cashpilot.R
import com.example.cashpilot.ui.components.GreenButton
import com.example.cashpilot.ui.components.StyledTextField
import com.example.cashpilot.viewmodel.RegistrationViewModel

@Composable
fun RegistrationScreen(navController: NavController, viewModel: RegistrationViewModel = viewModel()) {
    val registrationResult = viewModel.registrationResult.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.92f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(37.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_dollar),
                contentDescription = "Dollar Background",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = 0.25f },
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Регистрация",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            StyledTextField(
                value = viewModel.name.value,
                onValueChange = { viewModel.onNameChanged(it) },
                label = "Как Вас зовут?",
                isError = viewModel.nameError.value != null,
                errorMessage = viewModel.nameError.value
            )

            StyledTextField(
                value = viewModel.email.value,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = "Ваш e-mail",
                isError = viewModel.emailError.value != null,
                errorMessage = viewModel.emailError.value
            )

            StyledTextField(
                value = viewModel.password.value,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = "Пароль",
                isError = viewModel.passwordError.value != null,
                errorMessage = viewModel.passwordError.value
            )

            StyledTextField(
                value = viewModel.repeatPassword.value,
                onValueChange = { viewModel.onRepeatPasswordChanged(it) },
                label = "Повторите пароль",
                isError = viewModel.repeatPasswordError.value != null,
                errorMessage = viewModel.repeatPasswordError.value
            )


            Spacer(modifier = Modifier.height(32.dp))

            GreenButton(
                text = "Зарегистрироваться",
                onClick = {
                    viewModel.register()
                },
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            registrationResult?.let {
                Text(
                    text = it,
                    color = if (it.contains("успеш", true)) Color.Green else Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )

                if (it.contains("успеш", true)) {
                    // Переход на логин через задержку
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(1000)
                        navController.navigate("login")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "У меня уже есть аккаунт",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .clickable { navController.navigate("login") }
                    .padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen(navController = rememberNavController())
}
