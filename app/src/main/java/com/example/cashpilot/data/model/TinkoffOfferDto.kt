package com.example.cashpilot.data.model

data class TinkoffOfferDto(
    val loanName: String?,      // например "Кредит наличными"
    val depositName: String?,   // иногда может быть
    val attributes: List<TinkoffAttributeDto>
)

data class TinkoffAttributeDto(
    val subtitle: String?,      // label
    val title: String?          // value
)
