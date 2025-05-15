package com.example.cashpilot

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Welcome : Screen("welcome")
    object Main : Screen("main")
    object Credit : Screen("credit")
    object Deposit : Screen("deposit")
}
