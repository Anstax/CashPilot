package com.example.cashpilot.data.model

data class VtbDepositOfferDto(
    val bankName: String?,
    val depositName: String?,
    val attrs: List<Attribute>
)

data class Attribute(
    val label: String,
    val value: String
)
