package com.example.cashpilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cashpilot.ui.screens.*
import com.example.cashpilot.ui.theme.CashPilotTheme
import com.example.cashpilot.ui.screens.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CashPilotTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            RegistrationScreen(navController)
        }

        composable("main/{userName}") { backStackEntry ->
            val userNameArg = backStackEntry.arguments?.getString("userName") ?: ""
            MainScreen(navController, userName = userNameArg)
        }
        composable("credit/{userName}") { backStackEntry ->
            val userNameArg = backStackEntry.arguments?.getString("userName") ?: ""
            CreditScreen(navController, userName = userNameArg)
        }
        composable("deposit/{userName}") { backStackEntry ->
            val userNameArg = backStackEntry.arguments?.getString("userName") ?: ""
            DepositScreen(navController, userName = userNameArg)
        }

        composable("calculator/{userName}") { backStackEntry ->
            val userNameArg = backStackEntry.arguments?.getString("userName") ?: ""
            CalculatorScreen(navController, userName = userNameArg)
        }
    }

}

