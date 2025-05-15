// файл: TinkoffDepositApi.kt
package com.example.cashpilot.data.remote

import com.example.cashpilot.data.model.TinkoffOfferDto
import retrofit2.Response
import retrofit2.http.GET

interface TinkoffDepositApi {
    @GET("tbank-deposit-offers")  // или тот путь, который у тебя на Flask
    suspend fun getTinkoffDeposits(): Response<List<TinkoffOfferDto>>
}
