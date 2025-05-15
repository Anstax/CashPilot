// TinkoffCreditMapper.kt
package com.example.cashpilot.data.mapper

import com.example.cashpilot.data.model.CreditOfferDto
import com.example.cashpilot.data.model.TinkoffOfferDto




private fun parseSubtitle(subtitle: String): Pair<String, String> {
    return try {
        val amountPart = subtitle.substringBefore(" на срок").trim()
        val termPart = subtitle.substringAfter("на срок").trim()
        amountPart to termPart
    } catch (e: Exception) {
        "Не указано" to "Не указан"
    }
}

private fun defaultCreditOffer() = CreditOfferDto(
    bankName = "Тинькофф",
    minAmount = "Не указано",
    term = "Не указан",
    creditType = "Кредитное предложение"
)