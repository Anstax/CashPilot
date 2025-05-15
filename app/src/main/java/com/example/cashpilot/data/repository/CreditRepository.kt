package com.example.cashpilot.data.repository

import android.content.Context
import com.example.cashpilot.data.local.CreditOfferEntity
import com.example.cashpilot.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow
import com.example.cashpilot.data.AppDatabase

class CreditRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).creditOfferDao()

    fun offersStream(): Flow<List<CreditOfferEntity>> = dao.getAll()

    suspend fun refreshOffersFromNetwork() {
        val dtos = RetrofitClient.creditApi.getCreditOffers()
        val entities = dtos.map { dto ->
            CreditOfferEntity(
                bankName = dto.bankName,
                minAmount = dto.minAmount,
                term = dto.term,
                creditType = dto.creditType
            )
        }
        dao.clearAll()
        dao.insertAll(entities)
    }
}