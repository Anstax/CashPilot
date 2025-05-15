package com.example.cashpilot.data.model

data class DepositOfferDto(
    val bankName: String,
    val depositName: String,
    val rate: String?,      // nullable для Сбера
    val term: String?,
    val amount: String?,
    val attrs: List<VtbDepositAttrDto>? = null  // новый класс
)

data class VtbDepositAttrDto(
    val label: String,
    val value: String
)
