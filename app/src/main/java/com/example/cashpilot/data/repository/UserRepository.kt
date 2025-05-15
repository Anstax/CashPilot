package com.example.cashpilot.data.repository

import com.example.cashpilot.data.dao.UserDao
import com.example.cashpilot.data.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun registerUser(user: User): Boolean {
        val existing = userDao.getUserByEmail(user.email)
        if (existing != null) return false
        userDao.insert(user)
        return true
    }
}
