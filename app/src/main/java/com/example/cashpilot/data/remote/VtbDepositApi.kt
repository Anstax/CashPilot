package com.example.cashpilot.data.remote

import com.example.cashpilot.data.model.VtbDepositOfferDto
import retrofit2.Response
import retrofit2.http.GET

interface VtbDepositApi {
    @GET("vtb-deposit-offers")
    suspend fun getVtbDepositOffers(): Response<List<VtbDepositOfferDto>>
}
