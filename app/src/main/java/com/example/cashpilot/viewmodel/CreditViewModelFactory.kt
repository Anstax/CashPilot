package com.example.cashpilot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cashpilot.data.local.CreditOfferDao

class CreditViewModelFactory(private val dao: CreditOfferDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreditViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
