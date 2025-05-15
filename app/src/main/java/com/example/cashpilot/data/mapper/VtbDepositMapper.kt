package com.example.cashpilot.data.mapper

import com.example.cashpilot.data.model.Attribute
import com.example.cashpilot.data.model.DepositOfferDto
import com.example.cashpilot.data.model.VtbDepositOfferDto

fun VtbDepositOfferDto.toDepositOfferDto(): DepositOfferDto {
    // helper функция для поиска значения по label в attrs
    fun List<Attribute>.getValue(label: String): String =
        this.find { it.label == label }?.value ?: ""

    return DepositOfferDto(
        bankName = this.bankName ?: "ВТБ",
        depositName = this.depositName ?: "",
        rate = attrs.getValue("rate"),
        term = attrs.getValue("term"),
        amount = attrs.getValue("amount"),
        // Можно добавить другие поля или оставить пустыми
    )
}
