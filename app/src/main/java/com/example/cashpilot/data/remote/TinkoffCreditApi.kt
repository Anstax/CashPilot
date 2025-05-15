package com.example.cashpilot.data.remote

import com.example.cashpilot.data.model.CreditOfferDto
import com.example.cashpilot.data.model.TinkoffOfferDto
import retrofit2.http.GET

// TinkoffCreditApi.kt
interface TinkoffCreditApi {
    @GET("tbank-loan-offers")
    suspend fun getTinkoffCreditOffers(): List<TinkoffOfferDto> // Должен возвращать List
}
