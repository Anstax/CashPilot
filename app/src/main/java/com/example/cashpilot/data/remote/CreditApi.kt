package com.example.cashpilot.data.remote

import com.example.cashpilot.data.model.CreditOfferDto
import retrofit2.http.GET

interface CreditApi {
    @GET("credit-offers")
    suspend fun getCreditOffers(): List<CreditOfferDto>

}
