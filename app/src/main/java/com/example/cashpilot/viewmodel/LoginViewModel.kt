package com.example.cashpilot.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashpilot.data.AppDatabase
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getInstance(application).userDao()

    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var loginResult = mutableStateOf<String?>(null)
    var loggedInUserName = mutableStateOf<String?>(null)

    val emailError = mutableStateOf<String?>(null)
    val passwordError = mutableStateOf<String?>(null)

    fun onEmailChanged(value: String) {
        email.value = value
        emailError.value = null
    }

    fun onPasswordChanged(value: String) {
        password.value = value
        passwordError.value = null
    }

    private fun validate(): Boolean {
        var isValid = true

        emailError.value = if (email.value.isBlank()) {
            isValid = false
            "Введите e-mail"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            isValid = false
            "Некорректный e-mail"
        } else null

        passwordError.value = if (password.value.isBlank()) {
            isValid = false
            "Введите пароль"
        } else null

        return isValid
    }

    fun login(onSuccess: (String) -> Unit) {
        if (!validate()) return

        viewModelScope.launch {
            val user = userDao.getUserByEmail(email.value)
            loginResult.value = when {
                user == null -> "Пользователь не найден"
                user.password != password.value -> "Неверный пароль"
                else -> {
                    onSuccess(user.name)
                    "Успешный вход"
                }
            }
        }
    }
}
