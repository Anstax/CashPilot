package com.example.cashpilot.data.remote

import com.example.cashpilot.data.model.DepositOfferDto
import retrofit2.Response
import retrofit2.http.GET

interface DepositApi {
    @GET("/deposit-offers")
    suspend fun getDepositOffers(): Response<List<DepositOfferDto>>
}
