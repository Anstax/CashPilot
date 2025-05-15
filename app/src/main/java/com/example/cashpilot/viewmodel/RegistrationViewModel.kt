package com.example.cashpilot.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashpilot.data.AppDatabase
import com.example.cashpilot.data.model.User
import com.example.cashpilot.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository =
        UserRepository(AppDatabase.getInstance(application).userDao())

    var name = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var repeatPassword = mutableStateOf("")
    var registrationResult = mutableStateOf<String?>(null)

    val nameError = mutableStateOf<String?>(null)
    val emailError = mutableStateOf<String?>(null)
    val passwordError = mutableStateOf<String?>(null)
    val repeatPasswordError = mutableStateOf<String?>(null)

    fun onNameChanged(value: String) {
        name.value = value
        nameError.value = null
    }

    fun onEmailChanged(value: String) {
        email.value = value
        emailError.value = null
    }

    fun onPasswordChanged(value: String) {
        password.value = value
        passwordError.value = null
    }

    fun onRepeatPasswordChanged(value: String) {
        repeatPassword.value = value
        repeatPasswordError.value = null
    }

    private fun validate(): Boolean {
        var isValid = true

        nameError.value = if (name.value.isBlank()) {
            isValid = false
            "Введите имя"
        } else null

        emailError.value = if (email.value.isBlank()) {
            isValid = false
            "Введите e-mail"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            isValid = false
            "Некорректный e-mail"
        } else null

        passwordError.value = if (password.value.length < 6) {
            isValid = false
            "Пароль должен быть не менее 6 символов"
        } else null

        repeatPasswordError.value = if (repeatPassword.value != password.value) {
            isValid = false
            "Пароли не совпадают"
        } else null

        return isValid
    }

    fun register() {
        viewModelScope.launch {
            if (!validate()) return@launch

            val user = User(name = name.value, email = email.value, password = password.value)
            val success = repository.registerUser(user)

            registrationResult.value = if (success) "Успешная регистрация" else "Пользователь уже существует"
        }
    }
}
