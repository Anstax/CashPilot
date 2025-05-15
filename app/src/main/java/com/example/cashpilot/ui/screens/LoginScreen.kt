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
import com.example.cashpilot.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    val loginResult = viewModel.loginResult.value

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
                text = "Авторизация",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            StyledTextField(
                value = viewModel.email.value,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = "E-mail",
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


            Spacer(modifier = Modifier.height(32.dp))

            GreenButton(
                text = "Войти",
                onClick = {
                    viewModel.login { userName ->
                        navController.navigate("main/$userName")
                    }
                },
                modifier = Modifier.fillMaxWidth(0.9f)
            )


            loginResult?.let {
                Text(
                    text = it,
                    color = if (it.contains("успеш", true)) Color.Green else Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Зарегистрироваться",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .clickable { navController.navigate("register") }
                    .padding(top = 8.dp)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}
