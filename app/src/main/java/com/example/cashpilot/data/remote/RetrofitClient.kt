package com.example.cashpilot.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:5000/"

    // 1. Настраиваем клиент с увеличенными таймаутами
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)   // время на установление соединения
        .readTimeout(60, TimeUnit.SECONDS)      // время на чтение ответа
        .writeTimeout(60, TimeUnit.SECONDS)     // время на запись запроса
        .build()

    // 2. Создаём Retrofit с нашим клиентом
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val creditApi: CreditApi = retrofit.create(CreditApi::class.java)
    val depositApi: DepositApi = retrofit.create(DepositApi::class.java)
    val vtbDepositApi: VtbDepositApi = retrofit.create(VtbDepositApi::class.java)
    val tinkoffDepositApi: TinkoffDepositApi =
        retrofit.create(TinkoffDepositApi::class.java)
    val tinkoffCreditApi: TinkoffCreditApi = retrofit.create(TinkoffCreditApi::class.java)


}
